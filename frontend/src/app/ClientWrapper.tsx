'use client';

import Header from "@/components/common/Header";
import { usePathname } from 'next/navigation';
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

const queryClient = new QueryClient();

export default function ClientWrapper({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();

  const showHeader = !pathname.startsWith('/chat/');
  const mainClassName = showHeader ? 'pt-[57px]' : '';

  return (
    <QueryClientProvider client={queryClient}>
      {showHeader && <Header />}
      <main className={mainClassName}>{children}</main>
      {process.env.NODE_ENV === "development" && <ReactQueryDevtools initialIsOpen={false} />}
    </QueryClientProvider>
  );
}
