import UserPageHeader from '@/components/common/UserPageHeader';
import  Adverts  from '@/components/adverts/adverts';
import { getAdvertsWithSearchParameter } from '@/services/getAdverts-service';
import { auth } from '@/auth';
import { wait } from '@/utils/wait';


export const metadata = {
  title: 'PROPERTIES',
  description: 'Check out your dream properties'
};

export const Page = async ({ searchParams }) => {
  const session = await auth();

  const currentPage = Math.max(parseInt(searchParams.page, 10) || 1, 1);
  const size = searchParams.size || 10;
  const sort = searchParams.sort || 'createdAt';
  const type = searchParams.type || 'asc';
  const q = searchParams.q || '';
  const categoryId = searchParams.categoryId || '';
  const priceStart = searchParams.priceStart || '';
  const priceEnd = searchParams.priceEnd || '';
  const status = searchParams.status || '';
  const location = searchParams.location || '';


 await wait(4000);
 

  // Fetch adverts with search parameters
  const res = await getAdvertsWithSearchParameter(
    currentPage - 1,
    size,
    sort,
    type,
    q,
    categoryId,
    priceStart,
    priceEnd,
    status,
    location
  );

  return (
    <>
      <UserPageHeader text="PROPERTIES" />
      <div>
        <Adverts
          res={res}
          session={session}
          size={size}
          page={currentPage}
          sort={sort}
          type={type}
        />
      </div>
    </>
  );
};

export default Page;
