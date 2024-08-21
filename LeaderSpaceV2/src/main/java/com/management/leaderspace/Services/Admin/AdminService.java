package com.management.leaderspace.Services.Admin;


import com.management.leaderspace.Entities.Admin;
import com.management.leaderspace.Entities.Manager;

public interface AdminService {
    Admin getProfile();

    void saveManager(Manager manager);

    void saveUpdateManager(Manager manager);

    void saveAdmin(Admin admin);

    void saveUpdateAdmin(Admin admin);
}
