import React from 'react'
import AdminUser from '@/components/admin/user/AdminUser'
import { wait } from '@/utils/wait'




export default async function AdminUsersPage() {
  await wait(2000);
  return (
    <>
    <AdminUser/>
    </>
  )
}
