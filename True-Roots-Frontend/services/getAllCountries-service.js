'use server';
import { errorObject } from '@/helpers/functions/error-object';
import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getAllCountries = async () => {
  try {
    const response = await fetch(`${API_URL}/countries`);

    if (!response.ok) return errorObject('Failed to get the countries.');

    const data = await response.json();
    
    return data;

    
  } catch (error) {
    return errorObject('There was an error getting countries.');
  }
};