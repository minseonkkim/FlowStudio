"use client";

import React, { useEffect, useRef } from "react";

const DynamicHtmlRenderer = () => {
    const containerRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        // public/demo/index.html 파일 가져오기
        fetch("/demo/index.html")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch HTML file.");
                }
                return response.text();
            })
            .then((htmlContent) => {
                if (containerRef.current) {
                    const containerEl = containerRef.current;

                    // HTML 컨텐츠를 직접 추가
                    containerEl.innerHTML = htmlContent;

                    // 스크립트 수동 실행
                    const scripts = containerEl.getElementsByTagName("script");
                    Array.from(scripts).forEach((script) => {
                        if (script.innerHTML) {
                            // 스크립트 내용 수동 실행
                            window.eval(script.innerHTML);
                        }
                    });
                }
            })
            .catch((error) => console.error("Error fetching HTML:", error));
    }, []);

    return <div ref={containerRef} />;
};

export default DynamicHtmlRenderer;
