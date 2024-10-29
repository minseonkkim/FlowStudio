'use client';

import Header from "@/components/common/Header";
import { usePathname } from 'next/navigation';

export default function ClientWrapper({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();

  const showHeader = !pathname.startsWith('/chat/');
  const mainClassName = showHeader ? 'pt-[57px]' : '';

  return (
    <>
      {showHeader && <Header />}
      <main className={mainClassName}>{children}</main>
    </>
  );
}
