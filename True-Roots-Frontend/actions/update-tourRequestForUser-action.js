'use server';
import { getAuthHeader } from '@/helpers/auth-helper';

import { config } from '@/helpers/config';
const API_URL = config.api.baseUrl;

export async function updateTourRequest(id, formData) {
  try {
    const response = await fetch(`${API_URL}/tour-requests/${id}/auth`, {
      method: 'PUT',
      headers: await getAuthHeader(),
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
