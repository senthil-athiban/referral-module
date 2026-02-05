import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import api from '../../lib/api'
import { IncentiveRule } from '../../types'
import { useAuth } from '../../context/auth-context'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '../ui/dialog'
import { Button } from '../ui/button'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '../ui/form'
import { Input } from '../ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select'
import { Plus, Loader2 } from 'lucide-react'

const ruleSchema = z.object({
  name: z.string().min(3, 'Name is required'),
  description: z.string().min(5, 'Description is required'),
  referralTargetType: z.enum(['PROVIDER', 'EXTERNAL_PROVIDER']),
  amount: z.number().min(1, 'Amount must be positive'),
})

type RuleFormValues = {
  name: string
  description: string
  referralTargetType: 'PROVIDER' | 'EXTERNAL_PROVIDER'
  amount: number
}

interface RuleModalProps {
  rule?: IncentiveRule
  trigger?: React.ReactNode
}

export function IncentiveRuleModal({ rule, trigger }: RuleModalProps) {
  const [open, setOpen] = useState(false)
  const { user } = useAuth()
  const queryClient = useQueryClient()
  const isEditing = !!rule

  const form = useForm<RuleFormValues>({
    resolver: zodResolver(ruleSchema),
    defaultValues: rule
      ? {
          name: rule.name,
          description: rule.description,
          referralTargetType: rule.referralTargetType as any, // backend uses ReceiverType string
          amount: rule.defaultAmount,
        }
      : {
          name: '',
          description: '',
          referralTargetType: 'PROVIDER',
          amount: 0,
        },
  })

  const mutation = useMutation({
    mutationFn: (values: RuleFormValues) => {
      if (isEditing && rule) {
        return api.patch(`/incentive-rule/${rule.id}?actedBy=${user?.id}`, {
          name: values.name,
          description: values.description,
          referralTargetType: values.referralTargetType,
          defaultAmount: values.amount,
        })
      }
      return api.post('/incentive-rule', {
        ...values,
        ownerProviderId: user?.id,
      })
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['incentive-rules'] })
      setOpen(false)
      if (!isEditing) form.reset()
    },
  })

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        {trigger || (
          <Button className="bg-blue-600 hover:bg-blue-700 shadow-md shadow-blue-100 gap-2">
            <Plus size={18} />
            Add Rule
          </Button>
        )}
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>
            {isEditing ? 'Update Incentive Rule' : 'Create Incentive Rule'}
          </DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form
            onSubmit={form.handleSubmit((v) => mutation.mutate(v))}
            className="space-y-4 pt-4"
          >
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Rule Name</FormLabel>
                  <FormControl>
                    <Input placeholder="e.g. Specialists Flat Fee" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Short description of the rule"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="referralTargetType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Referral Target Type</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="PROVIDER">Doctor</SelectItem>
                      <SelectItem value="LAB">Lab</SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="amount"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Amount (₹)</FormLabel>
                  <FormControl>
                    <Input
                      type="number"
                      {...field}
                      onChange={(e) =>
                        field.onChange(parseFloat(e.target.value) || 0)
                      }
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="flex gap-3 pt-4">
              <Button
                type="button"
                variant="outline"
                className="flex-1"
                onClick={() => setOpen(false)}
              >
                Cancel
              </Button>
              <Button
                type="submit"
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white"
                disabled={mutation.isPending}
              >
                {mutation.isPending ? (
                  <Loader2 className="h-4 w-4 animate-spin" />
                ) : isEditing ? (
                  'Update Rule'
                ) : (
                  'Create Rule'
                )}
              </Button>
            </div>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
