"use server";

import { config } from '@/helpers/config';
import { getAuthHeader } from '@/helpers/auth-helper';

const API_URL = config.api.baseUrl;

export const fetchUser = async () => {
  try {
    const response = await fetch(`${API_URL}/users/auth`, {
      method: 'GET',
      headers: await getAuthHeader()
    });
    if (!response.ok) {
      throw new Error('Failed to fetch user data');
    }
    return await response.json();
  } catch (error) {
    throw new Error("Connection error while fetching user data. Please try later");
  }
};

export const updateUser = async (payload) => {
    const response = await fetch(`${API_URL}/users/auth`, {
      method: 'PUT',
      headers: await getAuthHeader(),
      body: JSON.stringify(payload)
    });
    return response;
};

export const deleteUserAccount = async () => {
  try {
    const response = await fetch(`${API_URL}/users/auth`, {
      method: 'DELETE',
      headers: await getAuthHeader()
    });
    return response;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const changePassword = async (payload) => {
  const response = await fetch(`${API_URL}/users/auth`, {
    method: 'PATCH',
    headers: await getAuthHeader(),
    body: JSON.stringify(payload)
  });
  const data = await response.json();

  if (!response.ok) {
    return {success: false, message: data.message};
  }
  
  return {success: true, message: data.message};
};

export const getUserByAdvertId = async (advertId) => {
  try {
    const response = await fetch(
      `${API_URL}/users/getUserByAdvertIdAdmin/${advertId}`,
      {
        method: 'GET',
        headers: await getAuthHeader()
      }
    );
    if (!response.ok) {
      throw new Error(`Failed to fetch user by advert id: ${advertId}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching user by advert id:', error.message);
    throw error;
  }
}
