'use client'

import { useState, useEffect } from 'react'
import { BsFillPersonFill } from '@react-icons/all-files/bs/BsFillPersonFill';
import Image from 'next/image'
import PurpleButton from '@/components/common/PurpleButton';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getUserInfo, getCheckNickName } from '@/api/profile'
import { UserInfo } from '@/types/profile'
import { AxiosError } from 'axios';

export default function UserProfile() {
  const [nickname, setNickName] = useState<string | null>(null)
  const [initialNickname, setInitialNickname] = useState<string | null>(null)
  const [nicknameStatus, setNicknameStatus] = useState<string | null>(null)
  const [email, setEmail] = useState<string | null>(null)
  const [isEditing, setIsEditing] = useState<boolean>(false)
  const [profileImage, setProfileImage] = useState<string | null>(null)
  const [openAi, setOpenAi] = useState("12345-ABCDE-67890-FGHIJ")
  const [gemini, setGemini] = useState("ZYXWV-54321-UTSRQ-98765")
  const [claude, setClaude] = useState("09876-LMNOP-43210-KJIHG")
  const queryClient = useQueryClient();

  const { isLoading, isError, error, data: userInfo } = useQuery<UserInfo>({
    queryKey: ['userInfo'],
    queryFn: getUserInfo,
  });


  const updateNicname = useMutation({
    mutationFn: getCheckNickName,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["userInfo"] });
      setNicknameStatus("사용 가능한 닉네임 입니다."); 
    },
    onError: (error: AxiosError) => {
      if (error.response) {
        const status = error.response.status;
        const message = status === 400 ? "중복된 닉네임입니다. 다른 닉네임으로 변경하세요." : "닉네임 중복 확인에 실패했습니다.";
        setNicknameStatus(message);
      } else {
        alert("네트워크 오류가 발생했습니다. 다시 시도해주세요.");
      }
    },
  });
  

  useEffect(() => {
    if (userInfo) {
      setNickName(userInfo.nickname);
      setInitialNickname(userInfo.nickname); 
      setEmail(userInfo.username);
      setProfileImage(userInfo.profileImage);
    }
  }, [userInfo]);

  useEffect(() => {
    if (isError && error) {
      alert("내 정보를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  if (isLoading) return <div>is Loading...</div>;

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickName(e.target.value);
    setNicknameStatus(null); 
  }

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setProfileImage(URL.createObjectURL(e.target.files[0]))
    }
  }

  const handleSave = () => setIsEditing(false);

  const handleCheckNickname = () => {
    if (nickname && nickname !== initialNickname) {
      updateNicname.mutate(nickname);
    } else if (nickname === initialNickname) {
      setNicknameStatus("닉네임이 변경되지 않았습니다.");
    }
  }

  return (
    <>
      <div className="w-full max-w-[900px] border-2 rounded-lg py-8 px-10">
        <h2 className="font-semibold text-[24px] text-gray-700 mb-5">내 정보</h2>
        <div className="flex flex-col md:flex-row md:space-x-16">
          <div className="flex flex-row md:flex-col md:items-center items-end mt-1">
            <div className="relative w-[108px] h-[108px] mb-2 border rounded-xl overflow-hidden">
              {profileImage ? (
                <Image 
                  src={profileImage} 
                  alt="프로필 이미지" 
                  fill
                  className="object-cover"
                />
              ) : (
                <BsFillPersonFill className="w-full h-full p-4 text-gray-400 bg-gray-300" />
              )}
            </div>

            {isEditing && (
              <div className="ml-4 mb-4 md:ml-0 md:mt-2">
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleImageUpload}
                  className="hidden" 
                  id="image-upload"
                />
                <label
                  htmlFor="image-upload"
                  className="text-[14px] font-semibold text-gray-600 cursor-pointer border rounded px-4 py-2 transition-colors duration-200 hover:border-gray-600"
                >
                  변경
                </label>
              </div>
            )}
          </div>

          <div className="flex-1 mt-6 md:mt-0">
            <div className="flex items-center gap-x-4">
              <p className="mt-2 mb-2 w-[80px] font-semibold text-base text-gray-600">닉네임</p>
              {isEditing ? (
                <div className="flex items-center gap-x-2 w-full">
                  <input
                    type="text"
                    value={nickname || ""}
                    onChange={handleNicknameChange}
                    className="border rounded-md flex-grow px-[10px] py-1 text-base text-gray-700 leading-normal focus:border-2 focus:border-[#9A75BF] focus:outline-none"
                  />
                  <button
                    onClick={handleCheckNickname}
                    className="text-[12px] md:text-[14px] font-semibold text-gray-600 cursor-pointer border rounded px-4 py-1 transition-colors duration-200 hover:border-gray-600"
                  >
                    중복 확인
                  </button>
                </div>
              ) : (
                <p className="mt-2 mb-2 text-base text-gray-700">{nickname}</p>
              )}
            </div>

            {isEditing && nicknameStatus && (
              <p className="md:ml-24 text-[12px] mt-1 text-red-500">{nicknameStatus}</p>
            )}

            <div className="mt-2 mb-2 flex items-center gap-x-4">
              <p className="w-[80px] font-semibold text-base text-gray-600">이메일</p>
              <p className="text-base text-gray-700">{email}</p>
            </div>

            {[{ label: 'OpenAI', value: openAi, setValue: setOpenAi }, { label: 'Claude', value: claude, setValue: setClaude }, { label: 'Gemini', value: gemini, setValue: setGemini }].map(({ label, value, setValue }) => (
              <div key={label} className="flex items-center gap-x-4">
                <p className="mt-2 mb-2 w-[80px] font-semibold text-base text-gray-600">{label}</p>
                {isEditing ? (
                  <input
                    type="text"
                    value={value}
                    onChange={(e) => setValue(e.target.value)}
                    className="border rounded-md w-full px-[10px] py-1 text-base text-gray-700 leading-normal focus:border-2 focus:border-[#9A75BF] focus:outline-none"
                  />
                ) : (
                  <p className="mt-2 mb-2 text-base text-gray-700">{value}</p>
                )}
              </div>
            ))}
          </div>
        </div>

        <div className="flex justify-end mt-4">
          <PurpleButton text={isEditing ? "저장" : "수정"} onHandelButton={isEditing ? handleSave : () => setIsEditing(true)} />
        </div>
      </div>
    </>
  )
}
