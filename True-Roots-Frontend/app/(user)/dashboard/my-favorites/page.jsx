import React from 'react'
import { Spacer } from "@/components/common/spacer/spacer";
import MyFavorites from "@/components/dashboard/my-favorites/my-favorites";
import { brand } from "@/constants/brand";
import UserPageHeader from "@/components/common/UserPageHeader";
import { getFavoriteAll } from "@/services/favorite-service";


export const metadata = {
  title: {
    default: brand.title,
    template: `${brand.name} | My Favorites`
  }
};

const MyFavoritesPage = async (props) => {

  const searchParams = await props.searchParams;
  const currentPage = Math.max(parseInt(searchParams.page) || 1, 1);  
  const size = searchParams.size || 10;
  const sort = searchParams.sort || 'createdAt';
  const type = searchParams.type || 'asc';
  const searchTerm = searchParams.q || '';
  
  const res = await getFavoriteAll(currentPage-1, size, sort, type, searchTerm);
  return (
    <>
      <UserPageHeader text="My Favorites" />
      <MyFavorites data={res} />
      <Spacer />
    </>
  );
};

export default MyFavoritesPage;