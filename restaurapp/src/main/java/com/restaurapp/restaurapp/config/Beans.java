package com.restaurapp.restaurapp.config;

import com.restaurapp.restaurapp.domain.repository.*;
import com.restaurapp.restaurapp.service.category.CreateCategoryService;
import com.restaurapp.restaurapp.service.category.DeleteCategoryService;
import com.restaurapp.restaurapp.service.category.GetCategoryService;
import com.restaurapp.restaurapp.service.category.UpdateCategoryService;
import com.restaurapp.restaurapp.service.orderitem.CreateOrderItemService;
import com.restaurapp.restaurapp.service.orderitem.DeleteOrderItemService;
import com.restaurapp.restaurapp.service.orderitem.GetOrderItemService;
import com.restaurapp.restaurapp.service.orderitem.UpdateOrderItemService;
import com.restaurapp.restaurapp.service.orderticket.CreateOrderTicketService;
import com.restaurapp.restaurapp.service.orderticket.DeleteOrderTicketService;
import com.restaurapp.restaurapp.service.orderticket.GetOrederTicketService;
import com.restaurapp.restaurapp.service.orderticket.GetOrderItemsService;
import com.restaurapp.restaurapp.service.orderticket.UpdateOrderTicketService;
import com.restaurapp.restaurapp.service.phone.CreatePhoneService;
import com.restaurapp.restaurapp.service.phone.DeletePhoneService;
import com.restaurapp.restaurapp.service.phone.GetPhonesService;
import com.restaurapp.restaurapp.service.phone.UpdatePhoneService;
import com.restaurapp.restaurapp.service.product.CreateProductService;
import com.restaurapp.restaurapp.service.product.DeleteProductService;
import com.restaurapp.restaurapp.service.product.GetProductService;
import com.restaurapp.restaurapp.service.product.UpdateProductService;
import com.restaurapp.restaurapp.service.role.CreateRoleService;
import com.restaurapp.restaurapp.service.role.DeleteRoleService;
import com.restaurapp.restaurapp.service.role.GetRolesService;
import com.restaurapp.restaurapp.service.role.UpdateRoleService;
import com.restaurapp.restaurapp.service.status.CreateStatusService;
import com.restaurapp.restaurapp.service.status.DeleteStatusService;
import com.restaurapp.restaurapp.service.status.GetStatusService;
import com.restaurapp.restaurapp.service.status.UpdateStatusService;
import com.restaurapp.restaurapp.service.table.CreateTableService;
import com.restaurapp.restaurapp.service.table.DeleteTableService;
import com.restaurapp.restaurapp.service.table.GetTableService;
import com.restaurapp.restaurapp.service.table.UpdateTableService;
import com.restaurapp.restaurapp.service.user.CreateUserService;
import com.restaurapp.restaurapp.service.user.DeleteUserService;
import com.restaurapp.restaurapp.service.user.GetUserService;
import com.restaurapp.restaurapp.service.user.UpdateUserService;
import com.restaurapp.restaurapp.service.userphone.CreateUserPhoneService;
import com.restaurapp.restaurapp.service.userphone.DeleteUserPhoneService;
import com.restaurapp.restaurapp.service.userphone.GetUserPhoneService;
import com.restaurapp.restaurapp.service.notification.OrderNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Beans {
    
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        // Configurar para que ignore las propiedades lazy no inicializadas
        hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        hibernate6Module.configure(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
        mapper.registerModule(hibernate6Module);
        return mapper;
    }
    
    @Bean
    public CreateCategoryService createCategoryService(CategoryRepositoryJpa categoryRepositoryJpa) {
        return new CreateCategoryService(categoryRepositoryJpa);
    }

    @Bean
    public CreateRoleService createRoleService(RoleRepositoryJpa roleRepositoryJpa) {
        return new CreateRoleService(roleRepositoryJpa);
    }

    @Bean
    public CreatePhoneService createPhoneService(PhoneRepositoryJpa phoneRepositoryJpa) {
        return new CreatePhoneService(phoneRepositoryJpa);
    }

    @Bean
    public CreateStatusService createStatusService(StatusRepositoryJpa statusRepositoryJpa) {
        return new CreateStatusService(statusRepositoryJpa);
    }

    @Bean
    public CreateTableService createTableService(TableRepositoryJpa tableRepositoryJpa){
        return new CreateTableService(tableRepositoryJpa);
    }

    @Bean
    public CreateUserPhoneService createUserPhoneService(PhoneRepositoryJpa phoneRepositoryJpa, UserRepositoryJpa
            userRepositoryJpa, UserPhoneRepositoryJpa userPhoneRepositoryJpa){
        return new CreateUserPhoneService(phoneRepositoryJpa, userRepositoryJpa, userPhoneRepositoryJpa );
    }

    @Bean
    public CreateUserService createUserService(UserRepositoryJpa userRepositoryJpa, PasswordEncoder passwordEncoder){
        return new CreateUserService(userRepositoryJpa, passwordEncoder);
    }

    @Bean
    public CreateProductService createProductService(ProductRepositoryJpa productRepositoryJpa){
        return new CreateProductService(productRepositoryJpa);
    }

    @Bean
    public CreateOrderTicketService createOrderTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa,
                                                            TableRepositoryJpa tableRepositoryJpa,
                                                            UserRepositoryJpa userRepositoryJpa,
                                                            StatusRepositoryJpa statusRepositoryJpa,
                                                            OrderNotificationService notificationService){
        return new CreateOrderTicketService(orderTicketRepositoryJpa, tableRepositoryJpa, 
                                          userRepositoryJpa, statusRepositoryJpa, notificationService);
    }
    @Bean
    public CreateOrderItemService createOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa,
                                                         OrderTicketRepositoryJpa orderTicketRepositoryJpa){
        return new CreateOrderItemService(orderItemRepositoryJpa, orderTicketRepositoryJpa);
    }

    @Bean
    public GetCategoryService getCategoryService(CategoryRepositoryJpa categoryRepositoryJpa){
        return new GetCategoryService(categoryRepositoryJpa);
    }

    @Bean
    public GetPhonesService getPhonesService(PhoneRepositoryJpa phoneRepositoryJpa){
        return new GetPhonesService(phoneRepositoryJpa);
    }

    @Bean
    public GetRolesService getRolesService(RoleRepositoryJpa roleRepositoryJpa){
        return new GetRolesService(roleRepositoryJpa);
    }

    @Bean
    public GetStatusService getStatusService(StatusRepositoryJpa statusRepositoryJpa){
        return new GetStatusService(statusRepositoryJpa);
    }

    @Bean
    public GetTableService getTableService(TableRepositoryJpa tableRepositoryJpa){
        return new GetTableService(tableRepositoryJpa);
    }

    @Bean
    public GetUserService getUserService(UserRepositoryJpa userRepositoryJpa){
        return new GetUserService(userRepositoryJpa);
    }

    @Bean
    public GetUserPhoneService getUserPhoneService(UserPhoneRepositoryJpa userPhoneRepositoryJpa){
        return new GetUserPhoneService(userPhoneRepositoryJpa);
    }

    @Bean
    public GetProductService getProductService(ProductRepositoryJpa productRepositoryJpa){
        return new GetProductService(productRepositoryJpa);
    }
    @Bean
    public GetOrederTicketService getOrederTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa){
        return new GetOrederTicketService(orderTicketRepositoryJpa);
    }
    
    @Bean
    public GetOrderItemsService getOrderItemsService(OrderItemRepositoryJpa orderItemRepositoryJpa){
        return new GetOrderItemsService(orderItemRepositoryJpa);
    }
    @Bean
    public GetOrderItemService getOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa){
        return new GetOrderItemService(orderItemRepositoryJpa);
    }
    @Bean
    public UpdateCategoryService updateCategoryService(CategoryRepositoryJpa categoryRepositoryJpa){
        return new UpdateCategoryService(categoryRepositoryJpa);
    }
    @Bean
    public UpdatePhoneService updatePhoneService(PhoneRepositoryJpa phoneRepositoryJpa){
        return new UpdatePhoneService(phoneRepositoryJpa);
    }
    @Bean
    public UpdateRoleService updateRoleService(RoleRepositoryJpa roleRepositoryJpa){
        return new UpdateRoleService(roleRepositoryJpa);
    }
    @Bean
    public UpdateProductService updateProductService(ProductRepositoryJpa productRepositoryJpa){
        return new UpdateProductService(productRepositoryJpa);
    }
    @Bean
    public UpdateStatusService updateStatusService(StatusRepositoryJpa statusRepositoryJpa){
        return new UpdateStatusService(statusRepositoryJpa);
    }
    @Bean
    public UpdateUserService updateUserService(UserRepositoryJpa userRepositoryJpa){
        return new UpdateUserService(userRepositoryJpa);
    }
    @Bean
    public UpdateTableService updateTableService(TableRepositoryJpa tableRepositoryJpa){
        return new UpdateTableService(tableRepositoryJpa);
    }
    @Bean
    public UpdateOrderTicketService updateOrderTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa, 
                                                            StatusRepositoryJpa statusRepositoryJpa,
                                                            OrderNotificationService notificationService){
        return new UpdateOrderTicketService(orderTicketRepositoryJpa, statusRepositoryJpa, notificationService);
    }
    @Bean
    public UpdateOrderItemService updateOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa){
        return new UpdateOrderItemService(orderItemRepositoryJpa);
    }
    @Bean
    public DeleteCategoryService deleteCategoryService(CategoryRepositoryJpa categoryRepositoryJpa){
        return new DeleteCategoryService(categoryRepositoryJpa);
    }
    @Bean
    public DeletePhoneService deletePhoneService(PhoneRepositoryJpa phoneRepositoryJpa){
        return  new DeletePhoneService(phoneRepositoryJpa);
    }
    @Bean
    public DeleteRoleService deleteRoleService(RoleRepositoryJpa roleRepositoryJpa){
        return new DeleteRoleService(roleRepositoryJpa);
    }
    @Bean
    public DeleteUserPhoneService deleteUserPhoneService(UserPhoneRepositoryJpa userPhoneRepositoryJpa){
        return new DeleteUserPhoneService(userPhoneRepositoryJpa);
    }
    @Bean
    public DeleteStatusService deleteStatusService(StatusRepositoryJpa statusRepositoryJpa){
        return new DeleteStatusService(statusRepositoryJpa);
    }
    @Bean
    public DeleteTableService deleteTableService(TableRepositoryJpa tableRepositoryJpa){
        return new DeleteTableService(tableRepositoryJpa);
    }
    @Bean
    public DeleteUserService deleteUserService(UserRepositoryJpa userRepositoryJpa){
        return new DeleteUserService(userRepositoryJpa);
    }
    @Bean
    public DeleteProductService deleteProductService(ProductRepositoryJpa productRepositoryJpa){
        return new DeleteProductService(productRepositoryJpa);
    }
    @Bean
    public DeleteOrderTicketService deleteOrderTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa){
        return new DeleteOrderTicketService(orderTicketRepositoryJpa);
    }
    @Bean
    public DeleteOrderItemService deleteOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa){
        return new DeleteOrderItemService(orderItemRepositoryJpa);
    }
}


