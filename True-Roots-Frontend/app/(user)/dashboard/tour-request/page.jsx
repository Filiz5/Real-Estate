import UserPageHeader from '@/components/common/UserPageHeader';
import TourRequestTable from '@/components/dashboard/user-tour-request/tour-request-table';
import { getUserTourRequests } from '@/services/user-tour-request-service';

export default async function UserTourRequestsPage(props) {
  const searchParams = props.searchParams;
  const searchTerm = searchParams.q || '';
  const currentPage = Math.max(parseInt(searchParams.page) || 1, 1);
  const pageSize = searchParams.size || 10;
  const sort = searchParams.sort || 'createdAt';
  const type = searchParams.type || 'asc';



  const response = await getUserTourRequests(
    searchTerm,
    currentPage - 1,
    pageSize,
    sort,
    type
  );

  return (
    <div>
      <UserPageHeader text="MY TOUR REQUESTS" />
      <TourRequestTable
        data={response}
        size={pageSize}
        page={currentPage}
        sort={sort}
        type={type}
      />
    </div>
  );
}
