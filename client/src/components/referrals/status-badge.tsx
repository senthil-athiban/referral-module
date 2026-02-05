import { Badge } from '../ui/badge'
import { ReferralStatus, SettlementStatus } from '../../types'

export function ReferralStatusBadge({ status }: { status: ReferralStatus }) {
  const styles = {
    SENT: 'bg-blue-50 text-blue-700 border-blue-100',
    ACCEPTED: 'bg-indigo-50 text-indigo-700 border-indigo-100',
    REJECTED: 'bg-red-50 text-red-700 border-red-100',
    COMPLETED: 'bg-green-50 text-green-700 border-green-100',
  }

  return (
    <Badge variant="outline" className={styles[status]}>
      {status}
    </Badge>
  )
}

export function SettlementStatusBadge({
  status,
}: {
  status: SettlementStatus
}) {
  const styles = {
    PENDING: 'bg-amber-50 text-amber-700 border-amber-100',
    PARTIALLY_SETTLED: 'bg-blue-50 text-blue-700 border-blue-100',
    SETTLED: 'bg-green-50 text-green-700 border-green-100',
  }

  return (
    <Badge variant="outline" className={styles[status]}>
      {status.replace('_', ' ')}
    </Badge>
  )
}
