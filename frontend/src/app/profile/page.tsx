'use client'

import React, { useState, ChangeEvent } from 'react'
import { FaUserCircle } from '@react-icons/all-files/fa/FaUserCircle';

const Page = () => {
  const [nickname, setNickName] = useState<string>("김싸피")
  const [email, setEmail] = useState<string>('ssafy@naver.com')
  const [isEditing, setIsEditing] = useState<boolean>(false)
  const [profileImage, setProfileImage] = useState<string | null>(null)

  const handleNicknameChange = (e: ChangeEvent<HTMLInputElement>) => setNickName(e.target.value)

  const handleImageUpload = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setProfileImage(URL.createObjectURL(e.target.files[0]))
    }
  }

  return (
    <>
      <div className="px-24 py-14 flex justify-center">
        <div className="w-full max-w-[1200px] border rounded-lg p-8 shadow-sm">
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
      </div>
    </>
  )
}

export default Page
