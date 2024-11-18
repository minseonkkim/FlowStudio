import { NextResponse } from 'next/server';

export async function middleware(request) {
  const token = request.cookies.get('authToken')?.value;

  if (token && request.nextUrl.pathname === '/') {
    return NextResponse.redirect(new URL('/explore/chatbots', request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/'], 
};
