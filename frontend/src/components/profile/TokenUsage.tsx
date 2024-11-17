'use client'

import { useState, useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { getTokenUsage } from '@/api/profile';
import { TokenUsage } from '@/types/profile';
import { MdKeyboardArrowLeft } from '@react-icons/all-files/md/MdKeyboardArrowLeft';
import { MdKeyboardArrowRight } from '@react-icons/all-files/md/MdKeyboardArrowRight';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  ChartOptions,
} from 'chart.js';
import Loading from '../common/Loading';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

export default function Page() {
  const { isLoading, isError, error, data: TokenUsageResponse } = useQuery<TokenUsage[]>({
    queryKey: ['TokenUsageResponse'],
    queryFn: getTokenUsage,
  });

  const data = TokenUsageResponse || [];

  const [viewMode, setViewMode] = useState<'daily' | 'weekly'>('daily');
  const [currentDayIndex, setCurrentDayIndex] = useState(Math.max(0, Math.ceil((data.length - 14) / 14)));

  useEffect(() => {
    if (isError && error) {
      alert("토큰 사용량을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) {
      console.warn("Invalid date value:", dateString);
      return "Invalid Date"; // 오류 발생 시 기본값을 반환
    }
    return date.toISOString().split('T')[0];
  };

  const getDailyData = () => {
    const startIndex = currentDayIndex * 14;
    return data.slice(startIndex, startIndex + 14).map(d => ({
      ...d,
      date: formatDate(d.date), 
    }));
  };

  const getWeeklyData = () => {
    const weeklyData = [];
    for (let i = 0; i < data.length; i += 7) {
      const weekData = data.slice(i, i + 7);
      const avgUsage = weekData.reduce((sum, day) => sum + day.tokenUsage, 0) / weekData.length;
      weeklyData.push({ Date: formatDate(weekData[0].date), Usage: Math.round(avgUsage) });
    }
    return weeklyData;
  };

  const handlePrevious = () => currentDayIndex > 0 && setCurrentDayIndex(currentDayIndex - 1);
  const handleNext = () => (currentDayIndex + 1) * 14 < data.length && setCurrentDayIndex(currentDayIndex + 1);

  const chartData = {
    labels: viewMode === 'daily'
      ? getDailyData().map(d => d.date.slice(5)) 
      : getWeeklyData().map(d => d.Date.slice(5)),
    datasets: [{
      label: '토큰 사용량',
      data: viewMode === 'daily' ? getDailyData().map(d => d.tokenUsage) : getWeeklyData().map(d => d.Usage),
      borderColor: '#874aa5',
      backgroundColor: 'rgba(135, 74, 165, 0.2)',
      borderWidth: 2,
    }],
  };
  
  const yearLabel = viewMode === 'daily' 
    ? getDailyData()[0]?.date.slice(0, 4) || '0' 
    : getWeeklyData()[0]?.Date.slice(0, 4) || '0';
  
  const chartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: true, position: 'bottom' as const },
    },
    scales: {
      x: { 
        title: { 
          display: true, 
          text: `월-일 [${yearLabel}년]`,
        }
      },
      y: { title: { display: false, text: '사용량' }, beginAtZero: true },
    },
  };
  

  if (isLoading) 
  return <Loading/>;
  
  return (
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
  );
}
