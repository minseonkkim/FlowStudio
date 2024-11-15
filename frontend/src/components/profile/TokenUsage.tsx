'use client'

import { useState } from 'react'
import { MdKeyboardArrowLeft } from '@react-icons/all-files/md/MdKeyboardArrowLeft';
import { MdKeyboardArrowRight } from '@react-icons/all-files/md/MdKeyboardArrowRight';
import { Line } from 'react-chartjs-2'
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend)

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

export default function Page() {
  const [viewMode, setViewMode] = useState<'daily' | 'weekly'>('daily')
  const [currentDayIndex, setCurrentDayIndex] = useState(Math.max(0, Math.ceil((data.length - 14) / 14)));

  const getDailyData = () => {
    const startIndex = currentDayIndex * 14
    return data.slice(startIndex, startIndex + 14)
  }

  const getWeeklyData = () => {
    const weeklyData = []
    for (let i = 0; i < data.length; i += 7) {
      const weekData = data.slice(i, i + 7)
      const avgUsage = weekData.reduce((sum, day) => sum + day.Usage, 0) / weekData.length
      weeklyData.push({ Date: weekData[0].Date, Usage: Math.round(avgUsage) })
    }
    return weeklyData
  }

  const handlePrevious = () => currentDayIndex > 0 && setCurrentDayIndex(currentDayIndex - 1)
  const handleNext = () => (currentDayIndex + 1) * 14 < data.length && setCurrentDayIndex(currentDayIndex + 1)

  const chartData = {
    labels: viewMode === 'daily'
      ? getDailyData().map(d => d.Date.slice(5))
      : getWeeklyData().map(d => d.Date.slice(5)),
    datasets: [{
      label: '토큰 사용량',
      data: viewMode === 'daily' ? getDailyData().map(d => d.Usage) : getWeeklyData().map(d => d.Usage),
      borderColor: '#874aa5',
      backgroundColor: 'rgba(135, 74, 165, 0.2)',
      borderWidth: 2,
    }],
  }

  const yearLabel = viewMode === 'daily' ? getDailyData()[0]?.Date.slice(0, 4) : getWeeklyData()[0]?.Date.slice(0, 4);

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false, 
    plugins: {
      legend: { display: true, position: 'bottom' as const },
    },
    scales: {
      x: { 
        title: { 
          display: true, 
          text: `월-일 [${yearLabel}년]` 
        }
      },
      y: { title: { display: false, text: '사용량' }, beginAtZero: true },
    },
  }

  return (
    <>
      <div className="w-full max-w-[900px] border-2 rounded-lg py-8 px-10">
        <div className="flex justify-between items-center mb-6">
          <h3 className="font-semibold text-[24px] text-gray-700">토큰 사용량</h3>
          <div className="flex border border-[#9A75BF] rounded-md overflow-hidden">
            <button
              onClick={() => setViewMode('daily')}
              className={`px-4 py-1.5 font-medium ${
                viewMode === 'daily' ? 'bg-[#9A75BF] text-white' : 'text-[#9A75BF]'
              }`}
            >
              일간
            </button>
            <button
              onClick={() => setViewMode('weekly')}
              className={`px-4 py-1.5 font-medium ${
                viewMode === 'weekly' ? 'bg-[#9A75BF] text-white' : 'text-[#9A75BF]'
              }`}
            >
              주간
            </button>
          </div>
        </div>

        <div className="flex justify-center items-center md:space-x-4">
          <button
            onClick={handlePrevious}
            disabled={currentDayIndex === 0}
            className={`rounded-full p-1 mb-16 transition-colors duration-200 ${
              viewMode === 'daily' 
                ? currentDayIndex === 0 
                  ? 'text-gray-300 cursor-not-allowed' 
                  : 'text-[#9A75BF]' 
                : 'invisible'
            }`}
          >
            <MdKeyboardArrowLeft className="text-4xl" />
          </button>

          <div className="w-full min-h-[450px]">
            <Line data={chartData} options={chartOptions} />
          </div>

          <button
            onClick={handleNext}
            disabled={(currentDayIndex + 1) * 14 >= data.length}
            className={`rounded-full p-1 mb-16 transition-colors duration-200 ${
              viewMode === 'daily' 
                ? (currentDayIndex + 1) * 14 >= data.length 
                  ? 'text-gray-300 cursor-not-allowed' 
                  : 'text-[#9A75BF]' 
                : 'invisible'
            }`}
          >
            <MdKeyboardArrowRight className="text-4xl" />
          </button>
        </div>
      </div>
    </>
  )
}