'use server';

import { config } from '@/helpers/config';
import { getAuthHeader } from '@/helpers/auth-helper';

const API_URL = config.api.baseUrl;

export async function fetchStatistics() {
  try {
    const response = await fetch(`${API_URL}/report`, {
      method: 'GET',
      headers: await getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error('Error fetching statistics');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching statistics:', error);
    throw error;
  }
}

export const fetchPopularAdverts = async (amount) => {
  const response = await fetch(`${API_URL}/report/most-popular-properties?amount=${amount}`, {
    method: 'GET',
    headers: await getAuthHeader(),
  });

  if (!response.ok) {
    throw new Error('Error fetching popular adverts');
  }

  return await response.json();
};

export const fetchAdvertsReport = async (params) => {
  const query = new URLSearchParams(params).toString();

  const response = await fetch(`${API_URL}/report/adverts?${query}`, {
    method: 'GET',
    headers: await getAuthHeader(),
  });

  if (!response.ok) {
    throw new Error('Error fetching adverts report');
  }

  return await response.json();
};

export const fetchReportUsers = async (role) => {
  const response = await fetch(`${API_URL}/report/users/${role}`, {
    method: 'GET',
    headers: await getAuthHeader(),
  });

  if (!response.ok) {
    throw new Error('Error fetching users report');
  }

  return await response.json();
};

export const fetchTourRequestReports = async (params) => {
  const query = new URLSearchParams(params).toString();
  const response = await fetch(`${API_URL}/report/tour-requests?${query}`, {
    method: 'GET',
    headers: await getAuthHeader(),
  });

  if (!response.ok) {
    throw new Error('Error fetching tour request reports');
  }

  return await response.json();
};

export const getCategoriesAsList = async () => {
  try {
    const response = await fetch(`${API_URL}/categories/list`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error(`Failed to fetch category: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error("Error fetching categories:", error.message);
    throw error;
  }
}
