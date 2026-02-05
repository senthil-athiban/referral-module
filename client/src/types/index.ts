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
  receiver: {
    type: ReceiverType
    providerId: string | null
    externalProviderId: string | null
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
