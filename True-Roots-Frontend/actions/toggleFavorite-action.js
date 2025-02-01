'use server';
import { auth } from '@/auth';
import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const toggleFavorite = async (adverId) => {
  try {
    const session = await auth();
    const token = session?.accessToken;

    const response = await fetch(`${API_URL}/favorites/${adverId}/auth`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`
      },
      cache: 'no-store'
    });

    if (!response.ok) {
      throw new Error(`Failed to toggle favorite: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error('Error toggle favorite:', error.message);
    throw error;
  }
};
