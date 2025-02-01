import TourRequestTable from '@/components/admin/admin-tour-request/tour-request-table';
import Paginations from '@/components/admin/admin-tour-request/admin-tour-request-pagination';
import { getAdminTourRequests } from '@/services/admin-tour-request';
import Spacer from '@/components/common/Spacer';
import { wait } from '@/utils/wait';

export default async function AdminTourRequestsPage(props) {
  const searchParams = props.searchParams;
  const searchTerm = searchParams.q || '';
  const currentPage = Math.max(parseInt(searchParams.page) || 1, 1);
  const pageSize = searchParams.size || 10;
  const sort = searchParams.sort || 'createdAt';
  const type = searchParams.type || 'asc';

  const response = await getAdminTourRequests(
    searchTerm,
    currentPage - 1,
    pageSize,
    sort,
    type
  );

  const totalPages = Math.ceil(response.totalPages);
  return (
    <div>
      <TourRequestTable data={response} />
      <Spacer height={17} />
      {
        totalPages === 0 ? null :
      
      <Paginations
        currentPage={currentPage}
        totalPages={totalPages}
        searchParams={searchParams}
      />}
    </div>
  );
}
