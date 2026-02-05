import { Referral } from '../../types'
import { Card, CardContent } from '../ui/card'
import { Button } from '../ui/button'
import { ReferralStatusBadge, SettlementStatusBadge } from './status-badge'
import { useAuth } from '../../context/auth-context'
import {
  Calendar,
  Stethoscope,
  ChevronRight,
  User,
  History,
} from 'lucide-react'
import { AddPaymentModal } from '../incentives/add-payment-modal'
import { Badge } from '../ui/badge'

interface ReferralCardProps {
  referral: Referral
  onAccept: (id: string) => void
  onReject: (id: string) => void
  isLoading?: boolean
}

export function ReferralCard({
  referral,
  onAccept,
  onReject,
  isLoading,
}: ReferralCardProps) {
  const { user } = useAuth()
  console.log('referral:', referral)

  const isReceiver = referral.receiver.providerId === user?.id
  const canAct = referral.status === 'SENT' && isReceiver

  return (
    <Card className="border-none shadow-sm hover:shadow-md transition-all overflow-hidden group">
      <CardContent className="p-6">
        <div className="flex flex-col md:flex-row gap-6">
          <div className="flex-1 space-y-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <div className="size-8 rounded-full bg-blue-50 flex items-center justify-center text-blue-600">
                  <User size={16} />
                </div>
                <div>
                  <h3 className="font-bold text-gray-900">
                    Patient ID: {referral.patientId.slice(0, 8)}
                  </h3>
                  <div className="flex items-center gap-4 text-xs text-gray-500 mt-0.5">
                    <span className="flex items-center gap-1">
                      <Calendar size={12} />{' '}
                      {new Date(referral.createdAt).toLocaleDateString()}
                    </span>
                    <span className="flex items-center gap-1">
                      <Stethoscope size={12} /> {referral.clinicalSummary}
                    </span>
                  </div>
                </div>
              </div>
              <div className="flex gap-2">
                <ReferralStatusBadge status={referral.status} />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-[1fr_auto_1fr] items-center gap-4 bg-white rounded-xl p-4 border border-gray-100 shadow-sm">
              <div className="space-y-1">
                <span className="text-[10px] text-gray-400 font-bold uppercase tracking-wider block">
                  Referrer
                </span>
                <div className="flex items-center gap-2">
                  <div className="size-7 rounded-full bg-blue-50 flex items-center justify-center text-blue-600 border border-blue-100">
                    <User size={14} />
                  </div>
                  <span className="text-sm font-bold text-gray-900">
                    {referral.referrerProvider?.name || 'Unknown'}
                  </span>
                </div>
              </div>

              <div className="hidden sm:flex items-center justify-center text-gray-300">
                <ChevronRight size={20} />
              </div>

              <div className="space-y-1">
                <div className="flex items-center gap-2">
                  <span className="text-[10px] text-gray-400 font-bold uppercase tracking-wider">
                    Receiver
                  </span>
                  <Badge
                    variant="outline"
                    className={`text-[8px] h-3.5 px-1 uppercase ${
                      referral.receiver.type === 'EXTERNAL_PROVIDER'
                        ? 'bg-purple-50 text-purple-600 border-purple-100'
                        : 'bg-green-50 text-green-600 border-green-100'
                    }`}
                  >
                    {referral.receiver.type === 'EXTERNAL_PROVIDER'
                      ? 'External'
                      : 'Internal'}
                  </Badge>
                </div>
                <div className="flex items-center gap-2">
                  <div
                    className={`size-7 rounded-full flex items-center justify-center border ${
                      referral.receiver.type === 'EXTERNAL_PROVIDER'
                        ? 'bg-purple-50 text-purple-600 border-purple-100'
                        : 'bg-green-50 text-green-600 border-green-100'
                    }`}
                  >
                    <Stethoscope size={14} />
                  </div>
                  <span className="text-sm font-bold text-gray-900">
                    {referral.receiver.type === 'PROVIDER'
                      ? referral.receiver.provider?.name
                      : referral.receiver.externalProvider?.name || 'Unknown'}
                  </span>
                </div>
              </div>
            </div>

            <div className="bg-gray-50 rounded-lg p-4 text-sm text-gray-600 border border-gray-100 italic">
              "{referral.clinicalNotes || 'No clinical notes provided.'}"
            </div>

            <div className="flex flex-wrap items-center gap-6 pt-2">
              <div className="flex flex-col text-xs">
                <span className="text-gray-400 font-medium uppercase tracking-wider mb-1">
                  Incentive Amount
                </span>
                <span className="text-lg font-bold text-gray-900">
                  ₹{referral.incentive?.netAmount}
                </span>
              </div>
              <div className="flex flex-col text-xs">
                <span className="text-gray-400 font-medium uppercase tracking-wider mb-1">
                  Payment Status
                </span>
                {referral.incentive?.settlementStatus && (
                  <SettlementStatusBadge
                    status={referral.incentive?.settlementStatus}
                  />
                )}
              </div>
              {referral.incentive?.id &&
                referral.incentive?.settlementStatus !== 'SETTLED' && (
                  <AddPaymentModal incentiveId={referral.incentive.id} />
                )}
            </div>

            {referral.incentive?.payments.length > 0 && (
              <div className="mt-4 pt-4 border-t border-gray-50">
                <div className="flex items-center gap-2 text-xs font-bold text-gray-400 uppercase tracking-widest mb-3">
                  <History size={12} /> Payment History
                </div>
                <div className="space-y-2">
                  {referral.incentive.payments.map((p) => (
                    <div
                      key={p.id}
                      className="flex items-center justify-between bg-gray-50/50 p-2 rounded border border-gray-100/50 text-xs"
                    >
                      <div className="flex items-center gap-3">
                        <Badge
                          variant="outline"
                          className="text-[10px] py-0 h-4 bg-white font-normal uppercase"
                        >
                          {p.paymentMode}
                        </Badge>
                        <span className="text-gray-500">{p.referenceId}</span>
                      </div>
                      <div className="flex items-center gap-4">
                        <span className="text-gray-400">
                          {new Date(p.paidAt).toLocaleDateString()}
                        </span>
                        <span className="font-bold text-gray-700">
                          ₹{p.amount}
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>

          <div className="md:w-48 flex flex-col justify-center gap-2 border-t md:border-t-0 md:border-l pt-4 md:pt-0 md:pl-6">
            {canAct ? (
              <>
                <Button
                  size="sm"
                  className="w-full bg-blue-600 hover:bg-blue-700"
                  onClick={() => onAccept(referral.id)}
                  disabled={isLoading}
                >
                  Accept Referral
                </Button>
                <Button
                  size="sm"
                  variant="outline"
                  className="w-full text-red-600 border-red-100 hover:bg-red-50 hover:text-red-700 hover:border-red-200"
                  onClick={() => onReject(referral.id)}
                  disabled={isLoading}
                >
                  Reject
                </Button>
              </>
            ) : (
              <Button
                size="sm"
                variant="ghost"
                className="w-full justify-between group-hover:bg-gray-50"
              >
                View Details
                <ChevronRight
                  size={16}
                  className="group-hover:translate-x-1 transition-transform"
                />
              </Button>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
