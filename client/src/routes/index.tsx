import { createFileRoute } from '@tanstack/react-router'
import { useAuth } from '../context/auth-context'
import {
  Users,
  ArrowUpRight,
  ArrowDownLeft,
  Clock,
  CheckCircle2,
  TrendingUp,
  Wallet,
} from 'lucide-react'
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card'
import { useQuery } from '@tanstack/react-query'
import api from '../lib/api'
import { Referral } from '../types'

export const Route = createFileRoute('/')({
  component: Dashboard,
})

function Dashboard() {
  const { user } = useAuth()

  const { data: referrals = [], isLoading } = useQuery<Referral[]>({
    queryKey: ['referrals', user?.id],
    queryFn: async () => {
      const response = await api.get(`/referral?providerId=${user?.id}`)
      return response.data
    },
    enabled: !!user?.id,
  })
  console.log('referrals:', referrals)

  const stats = [
    {
      title: 'Total Referrals',
      value: referrals.length,
      icon: Users,
      trend: '+12%',
      color: 'text-blue-600',
      bg: 'bg-blue-50',
    },
    {
      title: 'Pending Action',
      value: referrals.filter((r) => r.status === 'SENT').length,
      icon: Clock,
      trend: '4 needs review',
      color: 'text-amber-600',
      bg: 'bg-amber-50',
    },
    {
      title: 'Completed',
      value: referrals.filter((r) => r.status === 'COMPLETED').length,
      icon: CheckCircle2,
      trend: '+2 today',
      color: 'text-green-600',
      bg: 'bg-green-50',
    },
    {
      title: 'Earnings',
      value: `₹${referrals.reduce((acc, r) => acc + (r.incentive?.netAmount || 0), 0)}`,
      icon: Wallet,
      trend: 'Target: ₹50k',
      color: 'text-purple-600',
      bg: 'bg-purple-50',
    },
  ]

  return (
    <div className="space-y-8">
      <div>
        <h2 className="text-3xl font-bold tracking-tight text-gray-900">
          Dashboard
        </h2>
        <p className="text-gray-500 mt-1">
          Overview of your referral network and earnings.
        </p>
      </div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card
            key={stat.title}
            className="border-none shadow-sm hover:shadow-md transition-shadow"
          >
            <CardHeader className="flex flex-row items-center justify-between pb-2 space-y-0">
              <CardTitle className="text-sm font-medium text-gray-500">
                {stat.title}
              </CardTitle>
              <div className={`${stat.bg} p-2 rounded-lg`}>
                <stat.icon className={`size-5 ${stat.color}`} />
              </div>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
              <p className="text-xs text-gray-400 mt-1">
                <span className="text-green-500 font-medium inline-flex items-center gap-0.5">
                  <TrendingUp className="size-3" /> {stat.trend}
                </span>{' '}
                from last month
              </p>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-4 border-none shadow-sm">
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
          </CardHeader>
          <CardContent>
            {isLoading ? (
              <div className="space-y-4">
                {[1, 2, 3].map((i) => (
                  <div
                    key={i}
                    className="h-16 w-full animate-pulse bg-gray-50 rounded-lg"
                  />
                ))}
              </div>
            ) : referrals.length === 0 ? (
              <div className="flex flex-col items-center justify-center py-12 text-gray-500 italic">
                No recent referrals found.
              </div>
            ) : (
              <div className="space-y-6">
                {referrals.slice(0, 5).map((referral) => (
                  <div
                    key={referral.id}
                    className="flex items-center justify-between group"
                  >
                    <div className="flex items-center gap-4">
                      <div
                        className={`p-2 rounded-full ${referral.status === 'SENT' ? 'bg-blue-50' : 'bg-gray-50'}`}
                      >
                        {referral.status === 'SENT' ? (
                          <ArrowUpRight className="size-4 text-blue-600" />
                        ) : (
                          <ArrowDownLeft className="size-4 text-gray-600" />
                        )}
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">
                          Referral ID: {referral.id.slice(0, 8)}
                        </p>
                        <p className="text-xs text-gray-500">
                          {referral.clinicalSummary}
                        </p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium">
                        ₹{referral.incentive?.baseAmount}
                      </p>
                      <p className="text-xs text-gray-400">
                        {new Date(referral.createdAt).toLocaleDateString()}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>

        <Card className="col-span-3 border-none shadow-sm bg-linear-to-br from-slate-600 to-slate-700 text-white">
          <CardHeader>
            <CardTitle className="text-white font-bold">
              Quick Actions
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <button className="w-full flex items-center justify-between p-4 bg-white/10 hover:bg-white/20 rounded-xl transition-colors group">
              <span className="font-semibold">Create New Referral</span>
              <ArrowUpRight className="size-5 group-hover:translate-x-1 group-hover:-translate-y-1 transition-transform" />
            </button>
            <button className="w-full flex items-center justify-between p-4 bg-white/10 hover:bg-white/20 rounded-xl transition-colors group">
              <span className="font-semibold">Manage Incentives</span>
              <ArrowUpRight className="size-5 group-hover:translate-x-1 group-hover:-translate-y-1 transition-transform" />
            </button>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
