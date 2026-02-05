import { Link, useNavigate } from '@tanstack/react-router'
import { useAuth } from '../../context/auth-context'
import { Button } from '../ui/button'
import { LayoutDashboard, Users, BadgeDollarSign, LogOut } from 'lucide-react'

export function Sidebar() {
  const navigate = useNavigate()
  const { logout } = useAuth()

  const navItems = [
    { title: 'Dashboard', to: '/', icon: LayoutDashboard },
    { title: 'Referrals', to: '/referrals', icon: Users },
    { title: 'Incentives', to: '/incentives', icon: BadgeDollarSign },
  ]

  const handleLogout = () => {
    logout()
    navigate({ to: '/login' })
  }

  return (
    <div className="flex h-screen w-64 flex-col border-r bg-white">
      <div className="flex h-16 items-center border-b px-6">
        <Link
          to="/"
          className="flex items-center gap-2 font-bold text-xl text-blue-600"
        >
          <div className="size-8 rounded-lg bg-blue-600 flex items-center justify-center text-white">
            R
          </div>
          <span>ReferralApp</span>
        </Link>
      </div>

      <div className="flex-1 overflow-y-auto py-4 px-3">
        <nav className="space-y-1">
          {navItems.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              activeProps={{ className: 'bg-blue-50 text-blue-600 shadow-sm' }}
              className="flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium text-gray-700 transition-all hover:bg-gray-50 active:scale-[0.98]"
            >
              <item.icon className="size-5" />
              {item.title}
            </Link>
          ))}
        </nav>
      </div>

      <div className="border-t p-4">
        <Button
          variant="ghost"
          className="w-full justify-start gap-3 text-red-600 hover:bg-red-50 hover:text-red-700 font-medium"
          onClick={handleLogout}
        >
          <LogOut className="size-5" />
          Logout
        </Button>
      </div>
    </div>
  )
}
