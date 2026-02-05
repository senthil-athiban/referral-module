import { useAuth } from '../../context/auth-context'
import { Avatar, AvatarFallback } from '../ui/avatar'
import { Bell } from 'lucide-react'
import { Button } from '../ui/button'

export function Header() {
  const { user } = useAuth()

  return (
    <header className="flex h-16 items-center justify-between border-b bg-white px-6 shadow-sm sticky top-0 z-10">
      <h1 className="text-lg font-semibold text-gray-800">
        Welcome, {user?.name || 'Provider'}
      </h1>

      <div className="flex items-center gap-4">
        <Button
          variant="ghost"
          size="icon"
          className="text-gray-500 rounded-full hover:bg-gray-100"
        >
          <Bell className="size-5" />
        </Button>
        <div className="flex items-center gap-3 border-l pl-4">
          <div className="text-right">
            <p className="text-sm font-medium text-gray-900 leading-tight">
              {user?.name}
            </p>
            <p className="text-xs text-gray-500">{user?.email}</p>
          </div>
          <Avatar className="size-9 ring-2 ring-blue-50 ring-offset-1">
            <AvatarFallback className="bg-blue-600 text-white font-semibold">
              {user?.name
                ?.split(' ')
                .map((n) => n[0])
                .join('') || 'U'}
            </AvatarFallback>
          </Avatar>
        </div>
      </div>
    </header>
  )
}
