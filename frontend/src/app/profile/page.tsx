import UserInfo from '@/components/profile/UserInfo'
import TokenUsage from '@/components/profile/TokenUsage'

export default function Page() {
  return (
    <div className="px-4 md:px-12 py-10 flex flex-col items-center space-y-8">
      {/* 내 정보 */}
      <UserInfo />

      {/* 토큰 사용량 */}
      <TokenUsage />
    </div>
  )
}