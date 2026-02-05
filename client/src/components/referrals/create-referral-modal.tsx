import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import api from '../../lib/api'
import { useAuth } from '../../context/auth-context'
import { Patient, Provider, ExternalProvider, IncentiveRule } from '../../types'
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
import { Textarea } from '../ui/textarea'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select'
import { RadioGroup, RadioGroupItem } from '../ui/radio-group'
import { Plus, Loader2 } from 'lucide-react'

const referralSchema = z.object({
  patientId: z.string().min(1, 'Please select a patient'),
  receiverType: z.enum(['PROVIDER', 'EXTERNAL_PROVIDER']),
  receiverProviderId: z.string().optional(),
  receiverExternalProviderId: z.string().optional(),
  clinicalSummary: z.string().min(3, 'Summary is required'),
  clinicalNotes: z.string().optional(),
  appliedIncentiveRuleId: z.string().optional(),
})

export function CreateReferralModal() {
  const [open, setOpen] = useState(false)
  const { user } = useAuth()
  const queryClient = useQueryClient()

  const form = useForm<z.infer<typeof referralSchema>>({
    resolver: zodResolver(referralSchema),
    defaultValues: {
      receiverType: 'PROVIDER',
      clinicalSummary: '',
      clinicalNotes: '',
    },
  })

  const receiverType = form.watch('receiverType')

  // Queries
  const { data: patients = [] } = useQuery<Patient[]>({
    queryKey: ['patients'],
    queryFn: () => api.get('/patient').then((res) => res.data),
  })

  const { data: providers = [] } = useQuery<Provider[]>({
    queryKey: ['providers'],
    queryFn: () => api.get('/provider').then((res) => res.data),
  })

  const { data: externalProviders = [] } = useQuery<ExternalProvider[]>({
    queryKey: ['external-providers'],
    queryFn: () => api.get('/external-provider').then((res) => res.data),
  })

  const { data: incentiveRules = [] } = useQuery<IncentiveRule[]>({
    queryKey: ['incentive-rules', user?.id],
    queryFn: () =>
      api
        .get(`/incentive-rule?ownerProviderId=${user?.id}`)
        .then((res) => res.data),
    enabled: !!user?.id,
  })

  const mutation = useMutation({
    mutationFn: (values: any) =>
      api.post('/referral', {
        ...values,
        receiverType: 'PROVIDER',
        referrerProviderId: user?.id,
        createdByProviderId: user?.id,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['referrals'] })
      setOpen(false)
      form.reset()
    },
  })

  function onSubmit(values: z.infer<typeof referralSchema>) {
    mutation.mutate(values)
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="bg-blue-600 hover:bg-blue-700 shadow-md shadow-blue-100 gap-2">
          <Plus size={18} />
          Create Referral
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px] max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>New Patient Referral</DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onSubmit)}
            className="space-y-4 pt-4"
          >
            <FormField
              control={form.control}
              name="patientId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Select Patient</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Choose a patient" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {patients.map((p) => (
                        <SelectItem key={p.id} value={p.id}>
                          {p.name} ({p.age}y, {p.gender})
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="receiverType"
              render={({ field }) => (
                <FormItem className="space-y-3">
                  <FormLabel>Receiver Type</FormLabel>
                  <FormControl>
                    <RadioGroup
                      onValueChange={field.onChange}
                      defaultValue={field.value}
                      className="flex gap-4"
                    >
                      <FormItem className="flex items-center space-x-2 space-y-0">
                        <FormControl>
                          <RadioGroupItem value="PROVIDER" />
                        </FormControl>
                        <FormLabel className="font-normal cursor-pointer">
                          Internal
                        </FormLabel>
                      </FormItem>
                      <FormItem className="flex items-center space-x-2 space-y-0">
                        <FormControl>
                          <RadioGroupItem value="EXTERNAL_PROVIDER" />
                        </FormControl>
                        <FormLabel className="font-normal cursor-pointer">
                          External
                        </FormLabel>
                      </FormItem>
                    </RadioGroup>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {receiverType === 'PROVIDER' ? (
              <FormField
                control={form.control}
                name="receiverProviderId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Internal Provider</FormLabel>
                    <Select
                      onValueChange={field.onChange}
                      defaultValue={field.value}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Choose a provider" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {providers
                          .filter((p) => p.id !== user?.id)
                          .map((p) => (
                            <SelectItem key={p.id} value={p.id}>
                              {p.name}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />
            ) : (
              <FormField
                control={form.control}
                name="receiverExternalProviderId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>External Provider</FormLabel>
                    <Select
                      onValueChange={field.onChange}
                      defaultValue={field.value}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Search external providers" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {externalProviders.map((p) => (
                          <SelectItem key={p.id} value={p.id}>
                            {p.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />
            )}

            <FormField
              control={form.control}
              name="clinicalSummary"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Clinical Summary</FormLabel>
                  <FormControl>
                    <Input placeholder="e.g. Chronic knee pain" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="clinicalNotes"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Clinical Notes (Optional)</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Any specific observations or instructions..."
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="appliedIncentiveRuleId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Incentive Rule (Optional)</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a rule" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {incentiveRules.map((r) => (
                        <SelectItem key={r.id} value={r.id}>
                          {r.name} (₹{r.defaultAmount})
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
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
                className="flex-1 bg-blue-600 hover:bg-blue-700"
                disabled={mutation.isPending}
              >
                {mutation.isPending ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Creating...
                  </>
                ) : (
                  'Submit Referral'
                )}
              </Button>
            </div>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
