'use client'

import React, { useState } from 'react'
import { FaUserCircle } from '@react-icons/all-files/fa/FaUserCircle';
import { MdKeyboardArrowLeft } from '@react-icons/all-files/md/MdKeyboardArrowLeft';
import { MdKeyboardArrowRight } from '@react-icons/all-files/md/MdKeyboardArrowRight';

import { Line } from 'react-chartjs-2'
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend)

const Page = () => {
  const [nickname, setNickName] = useState<string>("김싸피")
  const [email, setEmail] = useState<string>('ssafy@naver.com')
  const [isEditing, setIsEditing] = useState<boolean>(false)
  const [profileImage, setProfileImage] = useState<string | null>(null)
  const [viewMode, setViewMode] = useState<'daily' | 'weekly'>('daily')
  const [currentDayIndex, setCurrentDayIndex] = useState(0)

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => setNickName(e.target.value)
  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setProfileImage(URL.createObjectURL(e.target.files[0]))
    }
  }

  const data = [
    { Date: "2024-08-01", Usage: 54 },
    { Date: "2024-08-02", Usage: 57 },
    { Date: "2024-08-03", Usage: 74 },
    { Date: "2024-08-04", Usage: 77 },
    { Date: "2024-08-05", Usage: 77 },
    { Date: "2024-08-06", Usage: 54 },
    { Date: "2024-08-07", Usage: 93 },
    { Date: "2024-08-08", Usage: 41 },
    { Date: "2024-08-09", Usage: 40 },
    { Date: "2024-08-10", Usage: 98 },
    { Date: "2024-08-11", Usage: 94 },
    { Date: "2024-08-12", Usage: 57 },
    { Date: "2024-08-13", Usage: 92 },
    { Date: "2024-08-14", Usage: 53 },
    { Date: "2024-08-15", Usage: 84 },
    { Date: "2024-08-16", Usage: 38 },
    { Date: "2024-08-17", Usage: 77 },
    { Date: "2024-08-18", Usage: 42 },
    { Date: "2024-08-19", Usage: 63 },
    { Date: "2024-08-20", Usage: 54 },
    { Date: "2024-08-21", Usage: 47 },
    { Date: "2024-08-22", Usage: 75 },
    { Date: "2024-08-23", Usage: 69 },
    { Date: "2024-08-24", Usage: 88 },
    { Date: "2024-08-25", Usage: 28 },
    { Date: "2024-08-26", Usage: 35 },
    { Date: "2024-08-27", Usage: 94 },
    { Date: "2024-08-28", Usage: 65 },
    { Date: "2024-08-29", Usage: 77 },
    { Date: "2024-08-30", Usage: 52 },
    { Date: "2024-08-31", Usage: 87 },
    { Date: "2024-09-01", Usage: 93 },
    { Date: "2024-09-02", Usage: 10 },
    { Date: "2024-09-03", Usage: 56 },
    { Date: "2024-09-04", Usage: 97 },
    { Date: "2024-09-05", Usage: 87 },
    { Date: "2024-09-06", Usage: 23 },
    { Date: "2024-09-07", Usage: 40 },
    { Date: "2024-09-08", Usage: 34 },
    { Date: "2024-09-09", Usage: 89 },
    { Date: "2024-09-10", Usage: 65 },
    { Date: "2024-09-11", Usage: 72 },
    { Date: "2024-09-12", Usage: 66 },
    { Date: "2024-09-13", Usage: 38 },
    { Date: "2024-09-14", Usage: 72 },
    { Date: "2024-09-15", Usage: 95 },
    { Date: "2024-09-16", Usage: 28 },
    { Date: "2024-09-17", Usage: 95 },
    { Date: "2024-09-18", Usage: 45 },
    { Date: "2024-09-19", Usage: 46 },
    { Date: "2024-09-20", Usage: 87 },
    { Date: "2024-09-21", Usage: 89 },
    { Date: "2024-09-22", Usage: 39 },
    { Date: "2024-09-23", Usage: 29 },
    { Date: "2024-09-24", Usage: 41 },
    { Date: "2024-09-25", Usage: 50 },
    { Date: "2024-09-26", Usage: 84 },
    { Date: "2024-09-27", Usage: 76 },
    { Date: "2024-09-28", Usage: 46 },
    { Date: "2024-09-29", Usage: 73 },
    { Date: "2024-09-30", Usage: 10 },
    { Date: "2024-10-01", Usage: 84 },
    { Date: "2024-10-02", Usage: 38 },
    { Date: "2024-10-03", Usage: 70 },
    { Date: "2024-10-04", Usage: 64 },
    { Date: "2024-10-05", Usage: 44 },
    { Date: "2024-10-06", Usage: 96 },
    { Date: "2024-10-07", Usage: 42 },
    { Date: "2024-10-08", Usage: 29 },
    { Date: "2024-10-09", Usage: 89 },
    { Date: "2024-10-10", Usage: 92 },
    { Date: "2024-10-11", Usage: 60 },
    { Date: "2024-10-12", Usage: 65 },
    { Date: "2024-10-13", Usage: 69 },
    { Date: "2024-10-14", Usage: 47 },
    { Date: "2024-10-15", Usage: 49 },
    { Date: "2024-10-16", Usage: 89 },
    { Date: "2024-10-17", Usage: 61 },
    { Date: "2024-10-18", Usage: 51 },
    { Date: "2024-10-19", Usage: 40 },
    { Date: "2024-10-20", Usage: 20 },
    { Date: "2024-10-21", Usage: 33 },
    { Date: "2024-10-22", Usage: 41 },
    { Date: "2024-10-23", Usage: 82 },
    { Date: "2024-10-24", Usage: 91 },
    { Date: "2024-10-25", Usage: 32 },
    { Date: "2024-10-26", Usage: 55 },
    { Date: "2024-10-27", Usage: 84 },
    { Date: "2024-10-28", Usage: 48 },
    { Date: "2024-10-29", Usage: 72 },
  ]

  const getDailyData = () => {
    const startIndex = currentDayIndex * 14
    const endIndex = startIndex + 14
    return data.slice(startIndex, endIndex)
  }

  const getWeeklyData = () => {
    const weeklyData = []
    for (let i = 0; i < data.length; i += 7) {
      const weekData = data.slice(i, i + 7)
      const averageUsage = weekData.reduce((sum, day) => sum + day.Usage, 0) / weekData.length
      weeklyData.push({ Date: weekData[0].Date, Usage: Math.round(averageUsage) })
    }
    return weeklyData
  }

  const handlePrevious = () => {
    if (currentDayIndex > 0) setCurrentDayIndex(currentDayIndex - 1)
  }

  const handleNext = () => {
    if ((currentDayIndex + 1) * 14 < data.length) setCurrentDayIndex(currentDayIndex + 1)
  }

  const chartData = {
    labels: viewMode === 'daily' ? getDailyData().map(d => d.Date) : getWeeklyData().map(d => d.Date),
    datasets: [
      {
        label: '토큰 사용량',
        data: viewMode === 'daily' ? getDailyData().map(d => d.Usage) : getWeeklyData().map(d => d.Usage),
        fill: true,
        backgroundColor: 'rgba(154, 117, 191, 0.3)',
        borderColor: 'rgba(154, 117, 191, 1)',
       
      }
    ],
  }

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: 'bottom' as const,
      },
    },
    scales: {
      x: {
        title: { display: true, text: '일자' },
      },
      y: {
        title: { display: false, text: '사용량' },
        beginAtZero: true,
      },
    },
  }

  return (
    <>
      <div className="px-24 py-14 flex flex-col justify-center items-center">
        <div className="w-full max-w-[1200px] border rounded-lg px-8 py-4 shadow-sm">
          <h2 className="font-semibold text-xl mb-6 ml-8">내 정보</h2>

          <div className="flex mb-8">
            <div className="flex flex-col items-center w-[150px]">
              {profileImage ? (
                <img src={profileImage} alt="프로필 이미지" className="w-24 h-24 rounded-full mb-2" />
              ) : (
                <FaUserCircle className="w-24 h-24 mb-2" />
              )}
              {isEditing && (
                <>
                  <input
                    type="file"
                    accept="image/*"
                    onChange={handleImageUpload}
                    className="hidden" 
                    id="image-upload"
                  />
                  <label
                    htmlFor="image-upload"
                    className="text-sm font-semibold text-gray-500 cursor-pointer border rounded px-4 py-1"
                  >
                    이미지 변경
                  </label>
                </>
              )}
            </div>

            <div className="ml-8 flex-1 grid grid-cols-1 gap-y-4">
              <div className="flex items-center">
                <p className="w-[80px] font-semibold text-base">닉네임</p>
                {isEditing ? (
                  <input
                    spellCheck="false"
                    type="text"
                    value={nickname}
                    onChange={handleNicknameChange}
                    className="border rounded-md w-60 px-3 py-2 text-gray-700"
                  />
                ) : (
                  <p className="text-base text-gray-600">{nickname}</p>
                )}
              </div>

              <div className="flex items-center">
                <p className="w-[80px] font-semibold text-base">이메일</p>
                <p className="text-base text-gray-600">{email}</p>
                
                <button
                  onClick={() => setIsEditing(!isEditing)}
                  className="border bg-[#DBDBDB] hover:bg-[#B0B0B0] rounded-md w-[80px] h-[35px] font-semibold ml-auto"
                >
                  {isEditing ? "저장" : "설정"}
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className="relative w-full max-w-[1200px] border rounded-lg px-8 py-4 shadow-sm mt-6">

          <div className='flex justify-between my-2'>
            <p className="font-semibold text-xl mt-2 ml-8">토큰 사용량</p>
            <div className="flex mb-4 border border-[#9A75BF] rounded-lg w-[100px] overflow-hidden">
              <button
                onClick={() => setViewMode('daily')}
                className={`flex-1 py-1 font-medium ${
                  viewMode === 'daily' 
                    ? 'bg-white text-[#9A75BF]' 
                    : 'text-[#D3B3E7]'
                }`}
                style={{
                  borderRight: "1px solid #D3B3E7" 
                }}
              >
                일간
              </button>
              <button
                onClick={() => setViewMode('weekly')}
                className={`flex-1 py-1 font-medium ${
                  viewMode === 'weekly' 
                    ? 'bg-white text-[#9A75BF]' 
                    : 'text-[#D3B3E7]'
                }`}
              >
                주간
              </button>
            </div>
          </div>

          <div className="flex justify-center w-full px-4 items-center">
            { viewMode === 'daily' && (
              <button onClick={handlePrevious} className="border bg-white px-2 py-2 rounded-full mb-16">
                <MdKeyboardArrowLeft className="text-3xl" />
              </button>
            )}
            <div className="relative w-full max-w-[1000px] h-[500px] ml-4">
              <Line data={chartData} options={chartOptions} />
            </div>
             { viewMode === 'daily' && (            
              <button onClick={handleNext} className="border bg-white px-2 py-2 rounded-full mb-16">
              <MdKeyboardArrowRight className="text-3xl" />
              </button> )} 
          </div>
        </div>
      </div>
    </>
  )
}

export default Page
