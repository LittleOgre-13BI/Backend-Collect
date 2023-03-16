package com.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapperservice.UserMapperService;
import com.po.User;
import com.service.UserService;
import com.utils.Response;
import com.vo.MyPage;
import com.vo.UserFormVO;
import com.vo.UserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserMapperService userMapperService;

    @Autowired
    private UserService userService;

//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() {
//    }

    /**
     * 用户登录单元测试
     * 分支一：用户名不正确
     * 分支二：用户类型不正确
     * 分支三：密码不正确
     */
    @Test
    void login() {
        String username = "wz1";
        String password = "1";
        String userType = "employee";
        User user = new User();
        user.setUsername(username);
        user.setUserType(userType);
        user.setPassword(password);

        Mockito.doReturn(null, user).when(userMapperService).selectOne(any());

        // case 1 用户名不正确
        Response response = userService.login(username, password, userType);
        assertEquals(500, response.get("code"));
        assertEquals("用户名不正确", response.get("msg"));

        // case 2 用户类型不正确
        userType = "employer";
        response = userService.login(username, password, userType);
        assertEquals(500, response.get("code"));
        assertEquals("用户不存在",response.get("msg"));

        // case 3 密码不正确
        password = "12";
        userType = "employee";
        response = userService.login(username, password, userType);
        assertEquals(500, response.get("code"));
        assertEquals("密码不正确",response.get("msg"));

        // case 4 登录成功
        password = "1";
        response = userService.login(username, password, userType);
        assertEquals(0, response.get("code"));

    }

    /**
     * 用户注册单元测试
     * 分支一：用户名是否被注册
     */
    @Test
    void register() {
        Mockito.doReturn(new User(), null).when(userMapperService).selectOne(any());

        // case 1 该用户名已被注册
        UserVO userVO = new UserVO();
        Response response = userService.register(userVO, "employee");
        assertEquals(500, response.get("code"));
        assertEquals("该用户名已被注册，请重新注册", response.get("msg"));

        // case 2 成功注册
        assertEquals(0, userService.register(userVO, "employee").get("code"));
    }


    /**
     * 用户更改密码单元测试
     * 分支一：用户不存在
     * 分支二：原密码不正确
     */
    @Test
    void updatePassword() {
        Mockito.doReturn(null).when(userMapperService).selectById(2L);
        User user = new User();
        user.setUid(1L);
        user.setPassword("234");
        Mockito.doReturn(user).when(userMapperService).selectById(1L);

        // case 1 用户不存在
        UserFormVO userFormVO = new UserFormVO();
        userFormVO.setUid(2L);
        assertEquals(500, userService.updatePassword(userFormVO).get("code"));
        assertEquals("用户不存在", userService.updatePassword(userFormVO).get("msg"));

        // case 2 原密码填写错误
        userFormVO.setUid(1L);
        userFormVO.setOldPassword("123");
        assertEquals(500, userService.updatePassword(userFormVO).get("code"));
        assertEquals("密码不正确", userService.updatePassword(userFormVO).get("msg"));

        // case 3 更改成功
        userFormVO.setOldPassword("234");
        assertEquals(0, userService.updatePassword(userFormVO).get("code"));
    }

    /**
     * 用户更改基本信息单元测试
     * 分支一：用户不存在
     */
    @Test
    void updateUser() {
        Mockito.doReturn(null, new User()).when(userMapperService).selectById(any());
        UserVO userVO = new UserVO();

        // case 1 用户不存在
        Response response = userService.updateUser(userVO);
        assertEquals(500, response.get("code"));
        assertEquals("用户不存在", response.get("msg"));

        // case 2 更改成功
        assertEquals(0, userService.updateUser(userVO).get("code"));
    }

    /**
     * 删除用户单元测试
     * 分支一：用户不存在
     */
    @Test
    void removeUser() {
        Mockito.doReturn(null, new User()).when(userMapperService).selectById(any());

        // case 1 用户不存在
        Response response = userService.removeUser(1L);
        assertEquals(500, response.get("code"));
        assertEquals("用户不存在", response.get("msg"));

        // case 2 更改成功
        assertEquals(0, userService.removeUser(2L).get("code"));
    }

    /**
     * 根据用户username和nickname模糊查询用户列表单元测试
     */
    @Test
    void searchUserByName() {
        UserVO userVO = new UserVO();
        long pageIndex = 1L;
        long pageSize= 10L;
        long totalCount = 100L;
        IPage<User> iPage = new Page<>(pageIndex, pageSize, totalCount);
        Mockito.doReturn(iPage).when(userMapperService).selectPage(any(), any());
        MyPage<UserVO> rslt = userService.searchUserByName(userVO, pageIndex, pageSize);

        // 查询分页情况
        assertEquals(1L, rslt.getCurrPage());
        assertEquals(10L, rslt.getPageSize());
        assertEquals(100L, rslt.getTotal());
        assertEquals(10L, rslt.getTotalPage());
    }

    /**
     * 根据用户uid查询用户单元测试
     * 分支一：用户不存在
     */
    @Test
    void searchUserByUid() {
        Mockito.doReturn(null).when(userMapperService).selectById(1L);
        User user = new User();
        user.setUid(2L);
        Mockito.doReturn(user).when(userMapperService).selectById(2L);

        // case 1 用户不存在
        assertEquals(500, userService.searchUserByUid(1L).get("code"));
        assertEquals("用户不存在",userService.searchUserByUid(1L).get("msg"));

        // case 2 查询成功
        assertEquals(0, userService.searchUserByUid(2L).get("code"));
        assertEquals(2L, JSON.parseObject(JSON.toJSONString(userService.searchUserByUid(2L).get("user")),
                UserVO.class).getUid());
    }

    /**
     * 判断用户是否存在单元测试
     */
    @Test
    void isUserExits() {
        User user = new User();
        Mockito.doReturn(null, user).when(userMapperService).selectById(any());
        // case 1 用户不存在
        assertFalse(userService.isUserExits(1L));
        // case 2 用户存在
        assertTrue(userService.isUserExits(2L));
    }
}