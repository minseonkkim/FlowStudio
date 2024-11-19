import SpinnerImg from "@/assets/common/loadingSpinner.gif";

export default function Loading(){
  return <div className="h-[calc(100vh-57px)] flex justify-center items-center">
    <img src={SpinnerImg.src} className="w-[160px] h-[160px] object-cover"/>
  </div>
}