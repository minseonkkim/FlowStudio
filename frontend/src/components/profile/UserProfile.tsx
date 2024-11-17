'use client'

import { useState, useEffect } from 'react'
import { BsFillPersonFill } from '@react-icons/all-files/bs/BsFillPersonFill';
import Image from 'next/image'
import PurpleButton from '@/components/common/PurpleButton';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getUserInfo, getCheckNickName, patchNickName, patchProfileImage, getApiKeys, putApiKeys } from '@/api/profile'
import { UserInfo, ApiKeys } from '@/types/profile'
import { AxiosError } from 'axios';
import WhiteButton from '../common/whiteButton';
import Loading from '../common/Loading';

export default function UserProfile() {
  const [nickname, setNickName] = useState<string | null>(null)
  const [nicknameStatus, setNicknameStatus] = useState<string | null>(null)
  const [isEditing, setIsEditing] = useState<boolean>(false)
  const [profileImage, setProfileImage] = useState<File | string | null>(null)
  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const [openAi, setOpenAi] = useState<string | null>(null)
  const [gemini, setGemini] = useState<string | null>(null)
  const [claude, setClaude] = useState<string | null>(null)
  const [clova, setClova] = useState<string | null>(null)
  const queryClient = useQueryClient();

  const { isLoading: isUserInfoLoading, isError: isUserInfoError, error: userInfoError, data: userInfo } = useQuery<UserInfo>({
    queryKey: ['userInfo'],
    queryFn: getUserInfo,
  });
  
  const { isLoading: isApiKeysLoading, isError: isApiKeysError, error: apiKeysError, data: apiKeys } = useQuery<ApiKeys>({
    queryKey: ['apiKeys'],
    queryFn: getApiKeys,
  });


  const updateNickname = useMutation({
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

  const saveNickname = useMutation({
    mutationFn: patchNickName,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["userInfo"] });
    },
    onError: () => {
      alert("닉네임 수정에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const updateProfileImage = useMutation({
    mutationFn: patchProfileImage,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["userInfo"] });
      if (previewImage) {
        URL.revokeObjectURL(previewImage);
        setPreviewImage(null);
        setIsEditing(false)
      }
    },
    onError: () => {
      alert("프로필 이미지 수정에 실패했습니다. 다시 시도해 주세요.");
      if (userInfo) {
        setPreviewImage(userInfo.profileImage);
        setIsEditing(true)
      }
    },
  });

  const updateApiKeys = useMutation({
    mutationFn: putApiKeys,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["apiKeys"] });
    },
    onError: () => {
      alert("Api keys 수정에 실패했습니다. 다시 시도해 주세요.");
    },
  });
  

  useEffect(() => {
    if (userInfo) {
      setNickName(userInfo.nickname);
      setPreviewImage(userInfo.profileImage);
    }
    if (apiKeys) {
      setOpenAi(apiKeys.openAiKey);
      setGemini(apiKeys.geminiKey);
      setClaude(apiKeys.claudeKey);
      setClova(apiKeys.clovaKey);
    }
  }, [userInfo, apiKeys]);

  useEffect(() => {
    if (isUserInfoError && userInfoError) {
      alert("내 정보를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    } else if (isApiKeysError && apiKeysError) {
      alert("Api key를 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isUserInfoError, userInfoError, isApiKeysError, apiKeysError]);


  if (isUserInfoLoading || isApiKeysLoading) return <Loading/>;

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickName(e.target.value);
    setNicknameStatus(null); 
  }

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setProfileImage(file); 
      setPreviewImage(URL.createObjectURL(file)); 
    }
  };

  const handleSave = () => {
    // 닉네임 수정
    if (nickname && nicknameStatus === "사용 가능한 닉네임 입니다." ) {
      saveNickname.mutate(nickname);
      setIsEditing(false);
    } else if ( nickname != userInfo?.nickname || nicknameStatus === "중복된 닉네임입니다. 다른 닉네임으로 변경하세요." ) {
      alert("닉네임 중복 확인을 시도해 주세요.")
    }

    // 프로필 이미지 수정
    if (profileImage instanceof File) {
      updateProfileImage.mutate(profileImage)
      setProfileImage(null)
    }

    // api key 수정
    if (apiKeys?.claudeKey !== claude || apiKeys.clovaKey !== clova || apiKeys.geminiKey !== gemini || apiKeys.openAiKey !== openAi) {
      const apiKeys = {
        openAiKey: openAi || "",
        claudeKey: claude || "",
        geminiKey: gemini || "",
        clovaKey: clova || ""
      }
      updateApiKeys.mutate(apiKeys);
      setIsEditing(false);
    }
  }

  const handleCheckNickname = () => {
    if (nickname && nickname !== userInfo?.nickname) {
      updateNickname.mutate(nickname);
    } else if (nickname === userInfo?.nickname) {
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
            {previewImage ? (
              <Image 
                src={previewImage} 
                alt="프로필 이미지 미리보기" 
                fill
                className="object-cover"
              />
            ) : userInfo?.profileImage ? (
              <Image 
                src={userInfo.profileImage} 
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
                <p className="mt-2 mb-2 text-base text-gray-700">{userInfo?.nickname}</p>
              )}
            </div>

            {isEditing && nicknameStatus && (
              <p className="md:ml-24 text-[12px] mt-1 text-red-500">{nicknameStatus}</p>
            )}

            <div className="mt-2 mb-2 flex items-center gap-x-4">
              <p className="w-[80px] font-semibold text-base text-gray-600">이메일</p>
              <p className="text-base text-gray-700">{userInfo?.username}</p>
            </div>

            {[{ label: 'OpenAI', key: 'openAiKey' as const, value: openAi, setValue: setOpenAi },
              { label: 'Claude', key: 'claudeKey' as const, value: claude, setValue: setClaude },
              { label: 'Gemini', key: 'geminiKey' as const, value: gemini, setValue: setGemini },
              { label: 'Clova', key: 'clovaKey' as const, value: clova, setValue: setClova }]
              .map(({ label, key, value, setValue }) => (
                <div key={label} className="flex items-center gap-x-4">
                  <p className="mt-2 mb-2 w-[80px] font-semibold text-base text-gray-600">{label}</p>
                  {isEditing ? (
                    <input
                      type="text"
                      value={value || ''}  // 상태 값을 사용 (null인 경우 빈 문자열로 대체)
                      onChange={(e) => setValue(e.target.value)}
                      className="border rounded-md w-full px-[10px] py-1 text-base text-gray-700 leading-normal focus:border-2 focus:border-[#9A75BF] focus:outline-none"
                    />
                  ) : (
                    <p className="mt-2 mb-2 text-base text-gray-700 font-bold">
                    {apiKeys?.[key] ? '****************************************' : ''}
                  </p>
                  
                  )}
                </div>
              ))}
          </div>
        </div>

        <div className="flex justify-end mt-4 gap-2">
          {isEditing ? (
            <>
              <WhiteButton text="취소" onHandelButton={() => setIsEditing(false)} />
              <PurpleButton text="저장" onHandelButton={handleSave} />
            </>
          ) : (
            <PurpleButton text="수정" onHandelButton={() => setIsEditing(true)} />
          )}
        </div>
      </div>
    </>
  )
}
