import UserProfile from '@/components/profile/UserProfile'
import TokenUsage from '@/components/profile/TokenUsage'

export default function Page() {
  return (
    <div className="px-4 md:px-12 py-10 flex flex-col items-center space-y-8">
      {/* 내 정보 */}
      <UserProfile />

      {/* 토큰 사용량 */}
      <TokenUsage />
    </div>
  )
}