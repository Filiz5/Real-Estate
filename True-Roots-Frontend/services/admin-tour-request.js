"use server";

import { getAuthHeader } from '@/helpers/auth-helper';

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getAdminTourRequests = async (
  searchTerm,
  currentPage = 0,
  pageSize,
  sort,
  type
) => {
  const qs = `q=${searchTerm}&page=${currentPage}&size=${pageSize}&sort=${sort}&type=${type}`;

  try {
    const response = await fetch(`${API_URL}/tour-requests/admin?${qs}`, {
      method: 'GET',
      headers: await getAuthHeader()
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch tour requests: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error('Error fetching tour requests:', error.message);
    throw error;
  }
};

export const getTourRequestById = async (id) => {
  try {
    const response = await fetch(`${API_URL}/tour-requests/${id}/admin`, {
      method: 'GET',
      headers: await getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch tour request: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error('Error fetching category by ID:', error.message);
    throw error;
  }
};

export async function deleteTourRequest(id) {
  try {
    const response = await fetch(`${API_URL}/tour-requests/${id}`, {
      method: 'DELETE',
      headers: await getAuthHeader()
      // cache: 'no-store'
    });

    if (!response.ok) {
      return { success: false, message: `Failed to delete tour request: ${response.statusText}` };
    }

    return { success: true, message: 'Tour request has been deleted.' };
  } catch (error) {
    console.error('Error deleting tour request:', error.message);
    throw error;
  }
}
