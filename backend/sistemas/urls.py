from .views import *
from django.urls.conf import path

urlpatterns = [
    # Urls para Sistema Operativo (Padre)
    path('api/', SistemaOperativoAPI.as_view(), name="so_get"),
    path('api/<int:id>/update', SistemaOperativoUpdate.as_view(), name="so_put"),
    path('api/<int:id>/delete', SistemaOperativoDelete.as_view(), name="so_delete"),
    # Urls para Aplicacion (Hijo)
    path('api/app/', AplicacionGetPost.as_view(), name="app_get"),
    path('api/app/<int:id>/update', AplicacionUpdate.as_view(), name="app_put"),
    path('api/app/<int:id>/delete', AplicacionDelete.as_view(), name="app_delete"),
]
