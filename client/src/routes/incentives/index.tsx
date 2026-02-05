import { createFileRoute } from '@tanstack/react-router'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import api from '../../lib/api'
import { useAuth } from '../../context/auth-context'
import { IncentiveRule } from '../../types'
import { IncentiveRuleModal } from '../../components/incentives/incentive-rule-modal'
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from '../../components/ui/card'
import { Button } from '../../components/ui/button'
import { Edit2, Power, ArrowRight, Wallet } from 'lucide-react'
import { Badge } from '../../components/ui/badge'

export const Route = createFileRoute('/incentives/')({
  component: IncentivesPage,
})

function IncentivesPage() {
  const { user } = useAuth()
  const queryClient = useQueryClient()

  const { data: rules = [], isLoading } = useQuery<IncentiveRule[]>({
    queryKey: ['incentive-rules', user?.id],
    queryFn: () =>
      api
        .get(`/incentive-rule?ownerProviderId=${user?.id}`)
        .then((res) => res.data),
    enabled: !!user?.id,
  })

  const deactivateMutation = useMutation({
    mutationFn: (id: string) =>
      api.post(`/incentive-rule/${id}/deactivate?actedBy=${user?.id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['incentive-rules'] })
    },
  })

  return (
    <div className="space-y-8 animate-in fade-in duration-700">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight text-gray-900">
            Incentives
          </h2>
          <p className="text-gray-500 mt-1">
            Configure your referral rewards and track payouts.
          </p>
        </div>
        <IncentiveRuleModal />
      </div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {isLoading ? (
          [1, 2, 3].map((i) => (
            <div
              key={i}
              className="h-48 w-full animate-pulse bg-white rounded-xl shadow-sm"
            />
          ))
        ) : rules.length === 0 ? (
          <Card className="col-span-full border-none shadow-sm py-12 flex flex-col items-center justify-center">
            <div className="size-16 rounded-full bg-blue-50 flex items-center justify-center text-blue-300 mb-4">
              <Wallet className="size-8" />
            </div>
            <p className="text-gray-500 font-medium">
              No incentive rules created yet.
            </p>
            <IncentiveRuleModal
              trigger={
                <Button variant="link" className="text-blue-600 mt-2">
                  Create your first rule
                </Button>
              }
            />
          </Card>
        ) : (
          rules.map((rule) => (
            <Card
              key={rule.id}
              className={`border-none shadow-sm hover:shadow-md transition-all group ${!rule.active ? 'opacity-60 grayscale-[0.5]' : ''}`}
            >
              <CardHeader className="pb-3">
                <div className="flex items-center justify-between">
                  <Badge
                    variant={rule.active ? 'secondary' : 'outline'}
                    className={
                      rule.active
                        ? 'bg-green-50 text-green-700 border-green-100'
                        : ''
                    }
                  >
                    {rule.active ? 'Active' : 'Inactive'}
                  </Badge>
                  <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                    <IncentiveRuleModal
                      rule={rule}
                      trigger={
                        <Button variant="ghost" size="icon" className="size-8">
                          <Edit2 className="size-4" />
                        </Button>
                      }
                    />
                    <Button
                      variant="ghost"
                      size="icon"
                      className="size-8 text-red-500 hover:text-red-600 hover:bg-red-50"
                      onClick={() => deactivateMutation.mutate(rule.id)}
                      disabled={!rule.active || deactivateMutation.isPending}
                    >
                      <Power className="size-4" />
                    </Button>
                  </div>
                </div>
                <CardTitle className="text-lg mt-2">{rule.name}</CardTitle>
                <CardDescription className="line-clamp-2 min-h-10">
                  {rule.description}
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="flex items-center justify-between mt-2 pt-4 border-t border-gray-50">
                  <div className="flex flex-col">
                    <span className="text-xs text-gray-400 font-medium uppercase tracking-wider">
                      Amount
                    </span>
                    <span className="text-2xl font-bold text-gray-900">
                      ₹{rule.defaultAmount}
                    </span>
                  </div>
                  <div className="text-right">
                    <span className="text-xs text-gray-400 font-medium uppercase tracking-wider">
                      Target
                    </span>
                    <p className="font-medium text-gray-700">
                      {rule.referralTargetType}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>

      <div className="mt-12 bg-blue-600 rounded-2xl p-8 text-white flex flex-col md:flex-row items-center justify-between gap-6 shadow-xl shadow-blue-500/20">
        <div className="space-y-2">
          <h3 className="text-2xl font-bold">Need help with rules?</h3>
          <p className="text-blue-100 max-w-md">
            Rules determine how much you pay or receive for each successful
            referral. You can create different rules for different specialists.
          </p>
        </div>
        <Button
          variant="secondary"
          className="bg-white text-blue-600 hover:bg-blue-50 font-bold px-8 h-12 gap-2"
        >
          Learn More <ArrowRight className="size-4" />
        </Button>
      </div>
    </div>
  )
}
