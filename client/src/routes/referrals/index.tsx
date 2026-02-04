import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/referrals/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/referrals/"!</div>
}
