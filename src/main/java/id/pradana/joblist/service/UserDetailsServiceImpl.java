package id.pradana.joblist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import id.pradana.joblist.dao.UserModelDao;
import id.pradana.joblist.model.UserModel;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserModelDao dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = dao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        UserDetails userDetail = User.builder().username(user.getUsername()).password(user.getPassword()).roles("USER")
                .build();
        return userDetail;
    }

}
