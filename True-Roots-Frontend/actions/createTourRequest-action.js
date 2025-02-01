'use server';
import { auth } from '@/auth';
import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const createTourRequest = async (formData) => {


  try {
    const session = await auth();
    const token = session?.accessToken;

    if (!token) {
      throw new Error('Authentication failed: Missing access token');
    }

    const response = await fetch(`${API_URL}/tour-requests`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    });

    const data = await response.json();

     if (!response.ok) {
      return { success: false, message: data.message };
    }

    return { success: true, message: data.message };
  } catch (error) {
    throw error;
  }
}
