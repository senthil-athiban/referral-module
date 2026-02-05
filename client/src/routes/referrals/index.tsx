import { createFileRoute } from '@tanstack/react-router'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import api from '../../lib/api'
import { useAuth } from '../../context/auth-context'
import { Referral } from '../../types'
import { ReferralCard } from '../../components/referrals/referral-card'
import { CreateReferralModal } from '../../components/referrals/create-referral-modal'
import { Search, Filter } from 'lucide-react'
import { Input } from '../../components/ui/input'
import { Button } from '../../components/ui/button'
import { useState } from 'react'

export const Route = createFileRoute('/referrals/')({
  component: ReferralListPage,
})

function ReferralListPage() {
  const { user } = useAuth()
  const queryClient = useQueryClient()
  const [search, setSearch] = useState('')
  console.log('user:', user)

  const { data: referrals = [], isLoading } = useQuery<Referral[]>({
    queryKey: ['referrals', user?.id],
    queryFn: async () => {
      const response = await api.get(`/referral?providerId=${user?.id}`)
      return response.data
    },
    enabled: !!user?.id,
  })

  const acceptMutation = useMutation({
    mutationFn: (id: string) =>
      api.post(`/referral/${id}/accept?actedBy=${user?.id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['referrals'] })
    },
  })

  const rejectMutation = useMutation({
    mutationFn: (id: string) =>
      api.post(`/referral/${id}/reject?actedBy=${user?.id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['referrals'] })
    },
  })

  const filteredReferrals = referrals.filter(
    (r) =>
      r.clinicalSummary.toLowerCase().includes(search.toLowerCase()) ||
      r.id.toLowerCase().includes(search.toLowerCase()),
  )

  return (
    <div className="space-y-8 animate-in fade-in duration-700">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-3xl font-bold tracking-tight text-gray-900">
            Referrals
          </h2>
          <p className="text-gray-500 mt-1">
            Manage all your incoming and outgoing patient referrals.
          </p>
        </div>
        <CreateReferralModal />
      </div>

      <div className="flex flex-col md:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 size-4 text-gray-400" />
          <Input
            placeholder="Search by ID or clinical summary..."
            className="pl-10 bg-white border-none shadow-sm"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <Button
          variant="outline"
          className="bg-white border-none shadow-sm gap-2"
        >
          <Filter className="size-4" />
          Filters
        </Button>
      </div>

      <div className="grid gap-6">
        {isLoading ? (
          [1, 2, 3].map((i) => (
            <div
              key={i}
              className="h-48 w-full animate-pulse bg-white rounded-xl shadow-sm"
            />
          ))
        ) : filteredReferrals.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 bg-white rounded-xl border border-dashed border-gray-200">
            <div className="size-16 rounded-full bg-gray-50 flex items-center justify-center text-gray-300 mb-4">
              <Search className="size-8" />
            </div>
            <p className="text-gray-500 font-medium">
              No referrals found matching your search.
            </p>
            <Button
              variant="link"
              className="text-blue-600 mt-2"
              onClick={() => setSearch('')}
            >
              Clear search filters
            </Button>
          </div>
        ) : (
          filteredReferrals.map((referral) => (
            <ReferralCard
              key={referral.id}
              referral={referral}
              onAccept={(id) => acceptMutation.mutate(id)}
              onReject={(id) => rejectMutation.mutate(id)}
              isLoading={acceptMutation.isPending || rejectMutation.isPending}
            />
          ))
        )}
      </div>
    </div>
  )
}
