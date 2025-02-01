'use server';

import { config } from '@/helpers/config';
import { auth } from '@/auth';
import { getAuthHeader } from '@/helpers/auth-helper';

const API_URL = config.api.baseUrl;

// Server action for getting advert type
export async function getAdvertTypeById(id) {
  try {
    const session = await auth();
    const token = session?.accessToken;

    const response = await fetch(`${API_URL}/advert-types/${id}`, {
      method: "GET",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      cache: 'no-store'
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch advert type: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error("Error fetching advert type:", error.message);
    throw error;
  }
}

// Server action for updating advert type
export async function updateAdvertType(id, advertTypeData) {
  try {
    const session = await auth();
    const token = session?.accessToken;

    const response = await fetch(`${API_URL}/advert-types/${id}`, {
      method: "PUT",
      headers: await getAuthHeader(),
      body: JSON.stringify({
        title: advertTypeData.title,
        builtIn: advertTypeData.builtIn || false
      })
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || `Failed to update advert type: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error("Error updating advert type:", error.message);
    throw error;
  }
}

// Server action for creating new advert type
export async function createAdvertType(advertTypeData) {
  try {

    const response = await fetch(`${API_URL}/advert-types`, {
      method: "POST",
      headers: await getAuthHeader(),
      body: JSON.stringify({
        title: advertTypeData.title,
        builtIn: advertTypeData.builtIn || false
      })
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || `Failed to create advert type: ${response.statusText}`);
    }

    const data = await response.json();
    return { ok: true, data: data.object, message: data.message };
  } catch (error) {
    console.error("Error creating advert type:", error.message);
    throw error;
  }
}

// Server action for deleting advert type
export async function deleteAdvertType(id) {
  try {
    const session = await auth();
    const token = session?.accessToken;

    const response = await fetch(`${API_URL}/advert-types/${id}`, {
      method: "DELETE",
      headers: await getAuthHeader(),
      cache: 'no-store'
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || `Failed to delete advert type: ${response.statusText}`);
    }

    return true;
  } catch (error) {
    console.error("Error deleting advert type:", error.message);
    throw error;
  }
} 