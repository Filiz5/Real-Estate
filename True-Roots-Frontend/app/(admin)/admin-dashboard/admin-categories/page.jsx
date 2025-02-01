import Loading from '@/app/(user)/loading';
import Paginations from '@/components/admin/category/category-pagination';
import CategoryTable from '@/components/admin/category/category-table';
import Spacer from '@/components/common/Spacer';
import { getCategories } from '@/services/category-service';
import { wait } from '@/utils/wait';
import { Suspense } from 'react';

export default async function AdminCategoriesPage(props) {
  const searchParams = await props.searchParams;
  const searchTerm = searchParams.q || '';
  const currentPage = Math.max(parseInt(searchParams.page) || 1, 1);
  const pageSize = searchParams.size || 10;
  const sort = searchParams.sort || 'createdAt';
  const type = searchParams.type || 'asc';
  
  const response = await getCategories(
    searchTerm,
    currentPage - 1,
    pageSize,
    sort,
    type,
  );


  const totalPages = Math.ceil(response.totalPages);
  
  //categories/admin?q=&page=0&size=20&sort=createdAt&type=asc
  return (
    <div>
      <Spacer height={20} />
      <CategoryTable data={response} />
      {
        totalPages === 0 ? null :
      <Paginations
              currentPage={currentPage}
              totalPages={totalPages}
              searchParams={searchParams}
      />
      }
    </div>
  );
}
