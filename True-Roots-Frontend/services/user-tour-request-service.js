"use server";

import { getAuthHeader } from '@/helpers/auth-helper';

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getUserTourRequests = async (searchTerm, currentPage=0, pageSize, sort, type) => {
    const qs = `q=${searchTerm}&page=${currentPage}&size=${pageSize}&sort=${sort}&type=${type}`;

    try {
        const response = await fetch(`${API_URL}/tour-requests/auth?${qs}`, {
            method: "GET",
            headers: await getAuthHeader(),
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch tour requests: ${response.statusText}`);
        }

        const data = await response.json();
        return data.object;
        
    } catch (error) {
        console.error("Error fetching tour requests:", error.message);
        throw new Error("Failed to fetch tour requests");

    }
};
