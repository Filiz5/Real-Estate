
import AdminContactMessages from '@/components/admin/contact-message/AdminContactMessages';
import { wait } from '@/utils/wait';
import React from 'react'

export default async function AdminContactMessagesPage() {
  await wait(2000);
  return (
    <>
      <AdminContactMessages />
    </>
  );
}
