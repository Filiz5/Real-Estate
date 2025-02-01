import { getAuthHeader } from '@/helpers/auth-helper';

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getTourRequestDetailForUser = async (id) => {
  try {
    const response = await fetch(`${API_URL}/tour-requests/${id}/auth`, {
      method: 'GET',
      headers: await getAuthHeader()
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch tour requests detail: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error("Error fetching tour requests detail by ID:", error.message);
    throw error;
  }
};