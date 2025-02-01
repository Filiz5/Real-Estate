"use server";

import { getAuthHeader } from "@/helpers/auth-helper";

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl; 

export const getFavoriteAll = async (
  page = 0,
  size = 10,
  sort = 'createdAt',
  type = 'asc',
  searchTerm) => {
    const qs = `page=${page}&size=${size}&sort=${sort}&type=${type}&q=${searchTerm}`;

  const response = await fetch(`${API_URL}/favorites/auth?${qs}`, {
    method: 'GET',
    headers: await getAuthHeader()
  });
  if (!response.ok) {
    throw new Error(`Error while fetching favorites`);  
  }

  const data = await response.json();

  return data;
};

// New method to toggle favorite
export const toggleFavorite = async (advertId) => { // New function
  const response = await fetch(`${API_URL}/favorites/${advertId}/auth`, {
    method: 'POST',
    headers: await getAuthHeader()
  });

  if (!response.ok) {
    throw new Error(`Error while toggling favorite`);
  }

  const data = await response.json();
  return data; // Return the response data
};