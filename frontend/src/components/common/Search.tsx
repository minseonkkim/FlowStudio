import { IoSearchOutline } from "@react-icons/all-files/io5/IoSearchOutline";

interface SearchProps {
  onSearchChange: (searchTerm:string) => void
}

export default function Search({ onSearchChange }: SearchProps) {
  return (
    <>
    <div className="relative w-60">
      <IoSearchOutline className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" />
      <input 
        type="text" 
        placeholder="검색" 
        className="w-full h-10 pl-10 pr-3 rounded-md border bg-[#EAECF0] focus:outline-none"
        onChange={(e) => onSearchChange(e.target.value)}
      />
    </div>
     </>
  );
}
