import AdminAdverts from "@/components/admin/admin-adverts/AdminAdverts";
import { getAdvertsAdmin } from "@/services/advert-service";
import { wait } from "@/utils/wait";


export default async function AdminAdvertsPage(props) {
  const searchParams = await props.searchParams;
  const searchTerm = searchParams.q || '';
  const categoryId = searchParams.categoryId || '';
  const advertType = searchParams.advertType || '';
  const status = searchParams.status || '';
  const priceStart = searchParams.priceStart || '';
  const priceEnd = searchParams.priceEnd || '';
  const currentPage = Math.max(parseInt(searchParams.page) || 1, 1);
  const pageSize = searchParams.size || 10;
  const sort = searchParams.sort || 'createdAt';
  const type = searchParams.type || 'asc';

  const response = await getAdvertsAdmin(
    searchTerm,
    categoryId,
    advertType,
    status,
    priceStart,
    priceEnd,
    currentPage - 1,
    pageSize,
    sort,
    type,
  );
  return (
    <>
      <AdminAdverts data={response} />
    </>
  )
}
