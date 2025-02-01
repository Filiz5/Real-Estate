'use server';
import { getAuthHeader } from '@/helpers/auth-helper';
import { auth } from '@/auth';
import { config } from '@/helpers/config';
const API_URL = config.api.baseUrl;

export async function cancelTourRequest(id) {
  try {
     const session = await auth();
     const token = session?.accessToken;

    const response = await fetch(`${API_URL}/tour-requests/${id}/cancel`, {
      method: 'PATCH',
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    const errorData = await response.json();

    if (!response.ok) {
       return { success: false, message: errorData.message };
    }

    return { success: true, message: errorData.message };
  } catch (error) {
    console.error('Error canceling tour request:', error.message);
    throw error;
  }
}
