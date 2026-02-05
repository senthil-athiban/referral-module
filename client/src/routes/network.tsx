import { createFileRoute } from '@tanstack/react-router'
import { useQuery } from '@tanstack/react-query'
import api from '../lib/api'
import { useAuth } from '../context/auth-context'
import { ProviderStats } from '../types'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '../components/ui/table'
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card'
import { Badge } from '../components/ui/badge'
export const Route = createFileRoute('/network')({
  component: NetworkPage,
})

function NetworkPage() {
  const { user } = useAuth()

  const { data: networkStats = [], isLoading } = useQuery<ProviderStats[]>({
    queryKey: ['network', user?.id],
    queryFn: async () => {
      const response = await api.get(`/network/${user?.id}`)
      return response.data
    },
    enabled: !!user?.id,
  })

  return (
    <div className="space-y-8 animate-in fade-in duration-700">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight text-gray-900">
            Provider Network
          </h2>
          <p className="text-gray-500 mt-1">
            Analyze your referral trends and financial relationships with other
            providers.
          </p>
        </div>
      </div>

      <Card className="border-none shadow-sm overflow-hidden">
        <CardHeader className="bg-gray-50/50">
          <CardTitle className="text-lg">Network Activity Summary</CardTitle>
        </CardHeader>
        <CardContent className="p-0">
          <Table>
            <TableHeader className="bg-gray-50/50">
              <TableRow>
                <TableHead className="w-[200px]">Provider Name</TableHead>
                <TableHead>Outgoing Referrals</TableHead>
                <TableHead>Incoming Referrals</TableHead>
                <TableHead className="text-right">Total Incentives</TableHead>
                <TableHead className="text-right">Balance Due</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {isLoading ? (
                [1, 2, 3].map((i) => (
                  <TableRow key={i}>
                    <TableCell colSpan={5}>
                      <div className="h-10 w-full animate-pulse bg-gray-50 rounded" />
                    </TableCell>
                  </TableRow>
                ))
              ) : networkStats.length === 0 ? (
                <TableRow>
                  <TableCell
                    colSpan={5}
                    className="h-40 text-center text-gray-500 italic"
                  >
                    No network activity found.
                  </TableCell>
                </TableRow>
              ) : (
                networkStats.map((stat) => (
                  <TableRow
                    key={stat.providerId}
                    className="group hover:bg-blue-50/30 transition-colors"
                  >
                    <TableCell className="font-semibold text-gray-900">
                      <div className="flex items-center gap-2">
                        <div className="size-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 text-xs font-bold">
                          {stat.providerName.charAt(0)}
                        </div>
                        {stat.providerName}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex flex-col gap-1">
                        <div className="flex items-center gap-2">
                          <span className="text-sm font-medium">
                            {stat.outgoing.total}
                          </span>
                          <span className="text-xs text-gray-400">Total</span>
                        </div>
                        <div className="flex gap-1.5 font-mono">
                          <Badge
                            variant="outline"
                            className="text-[10px] px-1 h-4 bg-green-50 text-green-700 border-green-100"
                          >
                            A: {stat.outgoing.accepted}
                          </Badge>
                          <Badge
                            variant="outline"
                            className="text-[10px] px-1 h-4 bg-blue-50 text-blue-700 border-blue-100"
                          >
                            C: {stat.outgoing.completed}
                          </Badge>
                        </div>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex flex-col gap-1">
                        <div className="flex items-center gap-2">
                          <span className="text-sm font-medium">
                            {stat.incoming.total}
                          </span>
                          <span className="text-xs text-gray-400">Total</span>
                        </div>
                        <div className="flex gap-1.5 font-mono">
                          <Badge
                            variant="outline"
                            className="text-[10px] px-1 h-4 bg-green-50 text-green-700 border-green-100"
                          >
                            A: {stat.incoming.accepted}
                          </Badge>
                          <Badge
                            variant="outline"
                            className="text-[10px] px-1 h-4 bg-blue-50 text-blue-700 border-blue-100"
                          >
                            C: {stat.incoming.completed}
                          </Badge>
                        </div>
                      </div>
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex flex-col items-end">
                        <span className="font-bold text-gray-900">
                          ₹{stat.incentive.totalAmount.toLocaleString()}
                        </span>
                        <span className="text-[10px] text-gray-400 uppercase tracking-tight">
                          Paid: ₹{stat.incentive.paidAmount.toLocaleString()}
                        </span>
                      </div>
                    </TableCell>
                    <TableCell className="text-right">
                      <span
                        className={`font-bold ${stat.incentive.pendingAmount > 0 ? 'text-amber-600' : 'text-green-600'}`}
                      >
                        ₹{stat.incentive.pendingAmount.toLocaleString()}
                      </span>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <div className="grid gap-6 md:grid-cols-3">
        <Card className="border-none shadow-sm bg-blue-50/50">
          <CardHeader className="pb-2">
            <CardTitle className="text-xs font-semibold text-blue-600 uppercase tracking-wider">
              Top Recipient
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-lg font-bold text-gray-900">
              {networkStats.sort(
                (a, b) => b.outgoing.total - a.outgoing.total,
              )[0]?.providerName || 'N/A'}
            </div>
            <p className="text-xs text-gray-500 mt-1">
              Provider you refer patients to most often.
            </p>
          </CardContent>
        </Card>

        <Card className="border-none shadow-sm bg-indigo-50/50">
          <CardHeader className="pb-2">
            <CardTitle className="text-xs font-semibold text-indigo-600 uppercase tracking-wider">
              Most Reliable Partner
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-lg font-bold text-gray-900">
              {networkStats.sort(
                (a, b) =>
                  b.incoming.completed +
                  b.outgoing.completed -
                  (a.incoming.completed + a.outgoing.completed),
              )[0]?.providerName || 'N/A'}
            </div>
            <p className="text-xs text-gray-500 mt-1">
              Highest completion rate in your network.
            </p>
          </CardContent>
        </Card>

        <Card className="border-none shadow-sm bg-purple-50/50">
          <CardHeader className="pb-2">
            <CardTitle className="text-xs font-semibold text-purple-600 uppercase tracking-wider">
              Total Network Volume
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-lg font-bold text-gray-900">
              ₹
              {networkStats
                .reduce((acc, curr) => acc + curr.incentive.totalAmount, 0)
                .toLocaleString()}
            </div>
            <p className="text-xs text-gray-500 mt-1">
              Sum of all incentives generated in this network.
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
