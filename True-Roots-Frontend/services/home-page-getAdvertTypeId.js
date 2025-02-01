'use server';
import { errorObject } from '@/helpers/functions/error-object';
import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getAdvertsWithSearchParameter_Home= async (
  page = 0,
  size = 10,
  sort = 'createdAt',
  type = 'asc',
  search,
  categoryId,
  advertType,
  priceStart,
  priceEnd,
  status,
  location
) => {
  // Query string oluşturma
  const qs = new URLSearchParams();

  qs.append('page', page);
  qs.append('size', size);
  qs.append('sort', sort);
  qs.append('type', type);

  if (search) qs.append('q', search);
  if (categoryId) qs.append('categoryId', categoryId);
  if (advertType) qs.append('advertType', advertType);
  if (priceStart) qs.append('priceStart', priceStart);
  if (priceEnd) qs.append('priceEnd', priceEnd);
  if (status) qs.append('status', status);
  if (location) qs.append('location', location);

  try {
    const response = await fetch(`${API_URL}/adverts?${qs.toString()}`);
    if (!response.ok) return errorObject('Failed to get the Adverts.');

    const data = await response.json();

    // Eğer location filtresi varsa, gelen verileri filtrele
    if (location) {
      const filteredContent = data?.object?.content?.filter(
        (advert) => Number(advert.city?.id) === Number(location)
      );

      // Filtrelenmiş veriyi döndür
      return {
        ...data,
        object: {
          ...data.object,
          content: filteredContent
        }
      };
    }

    // Eğer location filtresi yoksa orijinal veriyi döndür
    return data;
  } catch (error) {
    console.error('Error fetching adverts:', error);
    return errorObject('There was an error getting the Adverts.');
  }
};
