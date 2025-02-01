"use server";

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getAdvertTypes = async () => {
  try {
    const response = await fetch(`${API_URL}/advert-types`, {
      method: "GET",
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch advert types: ${response.statusText}`);
    }

    const data = await response.json();
    console.log('response', response);
    return data;
  } catch (error) {
    console.error("Error fetching advert types:", error.message);
    throw error;
  }
};

