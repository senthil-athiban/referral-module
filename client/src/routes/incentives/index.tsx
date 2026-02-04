import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/incentives/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/incentives/"!</div>
}
