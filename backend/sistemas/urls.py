from .views import *
from django.urls.conf import path

urlpatterns = [
    path('api/', SistemaOperativoAPI.as_view(), name="so_get"),
    path('api/<int:id>/update', SistemaOperativoUpdate.as_view(), name="so_put"),
    path('api/<int:id>/delete', SistemaOperativoDelete.as_view(), name="so_delete"),
]