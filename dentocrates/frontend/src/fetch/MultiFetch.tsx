import { token } from "../components/token/TokenDecoder";
import { useState } from "react";

export interface ApiResponse {
    data: any;
    status: number;
    message: string;
}

interface FetchResult {
    data: (url: string, method?: string, answerObject?: any) => Promise<any>;
    isLoading: boolean;
}

export const MultiFetch = (): FetchResult => {
    const [isLoading, setLoading] = useState<boolean>(true);

    const data = async (
        url: string,
        method?: string,
        answerObject?: any
    ): Promise<ApiResponse> => {
        try {
            setLoading(true);
            const headers: Record<string, string> = token()
                ? {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token()}`,
                }
                : {
                    "Content-Type": "application/json",
                };
            const response = await fetch((url[0] === "/" ? "" : "/") + url, {
                method: method ?? "GET",
                body: answerObject ? JSON.stringify(answerObject) : undefined,
                headers: headers,
            });

            if (response.ok) {
                setLoading(false);
            } else {
                const errorMessage = `Failed to ${method ?? "GET"} to table: ${url}`;
                console.error(errorMessage);
                throw new Error(errorMessage);
            }
            return await response.json();
        } catch (error) {
            console.error(error);
            throw error;
        }
    };

    return { data, isLoading };
};
