"use server";

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getDistricts = async () => {
  try {
    const response = await fetch(`${API_URL}/districts`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch districts: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error('Error fetching advert types:', error.message);
    throw error;
  }
};

export const getCountries = async () => {
  try {
    const response = await fetch(`${API_URL}/countries`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch countries: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error('Error fetching advert types:', error.message);
    throw error;
  }
};

export const getCities = async () => {
  try {
    const response = await fetch(`${API_URL}/cities`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(`Failed to fetch cities: ${response.statusText}`);
    }

    return data.object;
  } catch (error) {
    throw error;
  }
};

export const getCitiesForHomePage = async () => {
  try {
    const response = await fetch(`${API_URL}/adverts/cities`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(`${data.message}`);
    }

    return data;
  } catch (error) {
    console.error('Error fetching advert types:', error.message);
    throw error;
  }
}
