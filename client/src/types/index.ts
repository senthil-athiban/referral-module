export type ReceiverType = 'PROVIDER' | 'EXTERNAL_PROVIDER'

export type ReferralStatus = 'SENT' | 'ACCEPTED' | 'REJECTED' | 'COMPLETED'

export type SettlementStatus = 'PENDING' | 'PARTIALLY_SETTLED' | 'SETTLED'

export type PaymentMode = 'CASH' | 'UPI' | 'BANK_TRANSFER'

export interface Provider {
  id: string
  name: string
  phone: string
  email: string
}

export interface Patient {
  id: string
  name: string
  age: number
  gender: string
}

export interface ExternalProvider {
  id: string
  name: string
  phone: string
  email: string
}

export interface IncentivePayment {
  id: string
  amount: number
  paymentMode: PaymentMode
  referenceId: string
  paidAt: string
}

export interface IncentiveRecord {
  id: string
  baseAmount: number
  netAmount: number
  settlementStatus: SettlementStatus
  payments: IncentivePayment[]
}

export interface Referral {
  id: string
  patientId: string
  referrerProviderId: string
  referrerLocationId: string
  referrerProvider: Provider
  receiver: {
    type: ReceiverType
    providerId: string | null
    externalProviderId: string | null
    locationId: string | null
    provider: Provider | null
    externalProvider: ExternalProvider | null
  }
  clinicalSummary: string
  clinicalNotes: string
  status: ReferralStatus
  createdAt: string
  incentive: IncentiveRecord
}

export interface IncentiveRule {
  id: string
  ownerProviderId: string
  referralTargetType: ReceiverType
  name: string
  description: string | null
  defaultAmount: number
  active: boolean
  createdAt: string
}

export interface AuthResponse {
  email: string
  id: string
  name: string
}

export interface ProviderStats {
  providerId: string
  providerName: string
  outgoing: {
    total: number
    accepted: number
    rejected: number
    completed: number
  }
  incoming: {
    total: number
    accepted: number
    rejected: number
    completed: number
  }
  incentive: {
    totalAmount: number
    paidAmount: number
    pendingAmount: number
  }
}
